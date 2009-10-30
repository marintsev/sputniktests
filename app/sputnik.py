#!/usr/bin/python
# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


import models
import os.path
import math
import random
import re
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext.webapp import template


_CASE_CACHE_SIZE = 8192
_PLOT_CACHE_SIZE = 1024


class RandomCache(object):
  """A constant-size cache that evicts random elements."""

  def __init__(self, limit):
    self._index_map = {}
    self._inverse_index = limit * [None]
    self._vector = limit * [None]
    self._limit = limit

  def __contains__(self, key):
    return key in self._index_map

  def __getitem__(self, key):
    if not key in self._index_map:
      return None
    return self._vector[self._index_map[key]]

  def __setitem__(self, key, value):
    if key in self._index_map:
      self._vector[self._index_map[key]] = value
    else:
      # Pick an index for this element
      index = random.randint(0, self._limit)
      old_key = self._inverse_index[index]
      if old_key:
        # If the index was occupied we evict the previous value from
        # the index map
        del self._index_map[old_key]
      self._index_map[key] = index
      self._inverse_index[index] = key
      self._vector[index] = value


class Plotter(object):

  def __init__(self, app, names, scores, distances):
    self.app = app
    self.names = names
    self.scores = scores
    self.distances = distances

  def distance(self, a, b):
    if a == b:
      return 0
    elif a == -1:
      return self.scores[b]
    elif b == -1:
      return self.scores[a]
    elif a < b:
      return self.distances[b-1][a]
    else:
      return self.distances[a-1][b]

  def position(self):
    clusters = []
    for i in xrange(len(self.names)):
      clusters.append(Cluster(self, [i], []))
    iters = 0
    # Calculate a hierarchical clustering for the points.  Note that
    # this algorithm runs in O(n^3) (at least) so if we want to use
    # this for a large number of points it should be improved.  It can
    # definitely be made O(n^2).
    while len(clusters) > 1:
      iters += 1
      if iters == 20:
        break
      min_dist = None
      min_i = None
      min_j = None
      # Find the two clusters that are closest to each other.
      for i in xrange(len(clusters)):
        for j in xrange(i):
          if min_dist is None:
            min_dist = clusters[i].distance(clusters[j])
            min_i = i
            min_j = j
            continue
          a = clusters[i]
          b = clusters[j]
          dist = a.distance(b)
          if dist < min_dist:
            min_dist = dist
            min_i = i
            min_j = j
      # Remove the two clusters and add their combination
      left = clusters[min_i]
      right = clusters[min_j]
      next_values = left.values + right.values
      next = Cluster(self, next_values, [left, right])
      indices = [k for k in xrange(len(clusters)) if k != min_i and k != min_j]
      clusters = [clusters[k] for k in indices]
      clusters.append(next)
    root = clusters[0]
    points = self.place(root, 0.0, 0.0, 100.0, 100.0, True)
    cx = 50.0
    cy = 50.0
    center = Point(self, cx, cy, -1)
    points.append(center)
    self.adjust(points)
    points = points[:-1]
    for point in points:
      point.x -= (center.x - cx)
      point.y -= (center.y - cy)
    return points

  def place(self, cluster, min_x, min_y, max_x, max_y, by_x):
    if not cluster.children:
      # If this is a leaf we place it right in the middle of the
      # available area.
      x_midpoint = (max_x - min_x) / 2
      y_midpoint = (max_y - min_y) / 2
      p = Point(self, min_x + x_midpoint, min_y + y_midpoint, cluster.values[0])
      return [p]
    left_weight = cluster.children[0].weight()
    right_weight = cluster.children[1].weight()
    total_weight = left_weight + right_weight
    ratio = left_weight / total_weight
    if by_x:
      x_midpoint = (max_x - min_x) * ratio
      one = self.place(cluster.children[0], min_x, min_y, min_x + x_midpoint, max_y, False)
      two = self.place(cluster.children[1], min_x + x_midpoint, min_y, max_x, max_y, False)
    else:
      y_midpoint = (max_y - min_y) * ratio
      one = self.place(cluster.children[0], min_x, min_y, max_x, min_y + y_midpoint, True)
      two = self.place(cluster.children[1], min_x, min_y + y_midpoint, max_x, max_y, True)
    return one + two

  def dampen(self, pull, temp):
    dx = pull[0]
    dy = pull[1]
    length = math.sqrt(dx * dx + dy * dy)
    if length > temp:
      ratio = temp / length
    else:
      ratio = 1.0
    pull[0] *= ratio
    pull[1] *= ratio

  def adjust(self, points):
    iterations = 60
    for l in xrange(iterations):
      temperature = l / float(iterations)
      pulls = []
      for i in xrange(len(points)):
        pulls.append([])
        for j in xrange(i):
          pull = points[i].pull(points[j])
          self.dampen(pull, temperature)
          pulls[i].append(pull)
      for i in xrange(len(points)):
        for j in xrange(i):
          pull = pulls[i][j]
          points[i].x += pull[0]
          points[i].y += pull[1]
          points[j].x -= pull[0]
          points[j].y -= pull[1]


class Cluster(object):

  def __init__(self, plotter, values, children):
    self.plotter = plotter
    self.values = values
    self.children = children

  def distance(self, other):
    sum = 0.0
    count = 0.0
    for a in self.values:
      for b in other.values:
        sum += self.plotter.distance(a, b)
        count += 1
    return sum / count

  def weight(self):
    return float(len(self.values))

  def __str__(self):
    return str(self.values)


class Point(object):

  def __init__(self, app, x, y, type):
    self.app = app
    self.x = x
    self.y = y
    self.type = type

  def pull(self, other):
    ideal = self.plotter.distance(self.id, other.id) / 9
    dx = self.x - other.x
    dy = self.y - other.y
    if ideal == 0:
      return [-dx, -dy]
    dist = (ideal - math.sqrt(dx * dx + dy * dy)) / ideal
    return [dx * dist, dy * dist]

  def icon(self):
    return self.app.get_icon(self.type)

  def bullet(self):
    type = abs(int(self.x) + int(self.y)) % 2
    return self.app.get_icon('bullet%i' % (type + 1))


class Sputnik(object):

  def __init__(self):
    self._case_source_cache = RandomCache(_CASE_CACHE_SIZE)
    self._plot_cache = RandomCache(_PLOT_CACHE_SIZE)
    self._icon_cache = { }

  def do_404(self, req):
    req.error(404)

  def do_500(self, req, message):
    req.response.out.write(message)
    req.error(500)

  def get_dynamic_path(self, name):
    return os.path.join(os.path.dirname(__file__), 'dynamic', name)

  def get_template(self, name, attribs):
    path = self.get_dynamic_path(name)
    return template.render(path, attribs)

  def get_main_page(self, req):
    req.response.headers['Content-Type'] = 'text/html'
    version = models.Version.get()
    if not version:
      self.do_500(req, "No current version found")
      return
    current = version.current_suite
    suite = models.Suite.lookup(current)
    if not suite:
      self.do_500(req, "Suite '%s' not found" % current)
      return
    req.response.out.write(self.get_template('index.html', {
      'default_suite_json': suite.to_json()
    }))

  def get_debug_page(self, req):
    req.response.headers['Content-Type'] = 'text/html'
    req.response.out.write(self.get_template('debug.html', {
      'suites': models.Suite.all(),
      'cases': models.Case.all()
    }))

  def get_test_case_page(self, req, suite, serial):
    req.response.headers['Content-Type'] = 'text/html'
    key = (suite, serial)
    if not key in self._case_source_cache:
      case = models.Case.lookup(suite, int(serial))
      if not case:
        return self.do_404(req)
      source = case.source
      source = re.sub(r'\$ERROR', 'sputnikTestFailed', source)
      result = self.get_template('case.html', {
        'case': case,
        'source': source
      })
      self._case_source_cache[key] = result
    req.response.out.write(self._case_source_cache[key])

  def get_test_case_source(self, req, suite, serial):
    req.response.headers['Content-Type'] = 'text/javascript'
    case = models.Case.lookup(suite, int(serial))
    if not case:
      return self.do_404(req)
    req.response.out.write(case.source)

  def get_test_suite_json(self, req, suite):
    req.response.headers['Content-Type'] = 'text/plain'
    suite = models.Suite.lookup(suite)
    if not suite:
      return self.do_404(req)
    req.response.out.write(suite.to_json())

  def get_icon(self, type):
    path = self.get_dynamic_path('%s.svg' % type)
    if os.path.exists(path):
      f = open(path)
      c = f.read()
      f.close()
      c = re.sub(r"^(.|[\n])*<!--\s+begin\s+icon\s+-->", "", c, re.MULTILINE)
      c = re.sub(r"<!--\s+end\s+icon\s+-->(.|[\n])*$", "", c, re.MULTILINE)
      return c
    else:
      return ''

  def get_comparison_plot(self, req):
    req.response.headers['Content-Type'] = 'image/svg+xml'
    points = req.request.params.get('m', None)
    map = {
      'bullseye': self.get_icon('bullseye'),
      'tag': self.get_icon('tag')
    }
    if points:
      point_objs = []
      for p in points.split(':'):
        [type, point] = p.split('@')
        [x, y] = point.split(',')
        point_objs.append(Point(self, x, y, type))
      key = points
      map['draw_points'] = True
      map['points'] = point_objs
    else:
      key = 'none'
      map['draw_points'] = False
    plot = self.get_template('plot.svg', map)
    self._plot_cache[key] = plot
    req.response.out.write(self._plot_cache[key])


def dispatcher(method):
  class Dispatcher(webapp.RequestHandler):
    def get(self, *args):
      return method.__call__(self, *args)
  Dispatcher.__name__ = method.__name__
  return Dispatcher


def initialize_application():
  sputnik = Sputnik()
  return webapp.WSGIApplication([
      ('/', dispatcher(sputnik.get_main_page)),
      ('/debug.html', dispatcher(sputnik.get_debug_page)),
      (r'/cases/(\w+)/(\d+).html', dispatcher(sputnik.get_test_case_page)),
      (r'/cases/(\w+)/(\d+).js', dispatcher(sputnik.get_test_case_source)),
      (r'/suites/(\w+).js', dispatcher(sputnik.get_test_suite_json)),
      (r'/compare/plot.svg', dispatcher(sputnik.get_comparison_plot))
  ], debug=True)


application = initialize_application()


def main():
  run_wsgi_app(application)


if __name__ == "__main__":
  main()
