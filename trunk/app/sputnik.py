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


_DISABLE_CACHING = True


_CASE_CACHE_SIZE = 8192
_PLOT_CACHE_SIZE = 1024
_CHUNK_CACHE_SIZE = 256


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
    self._chunk_cache = { }
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
      source = re.sub(r'\$ERROR', 'testFailed', source)
      result = self.get_template('case.html', {
        'case': case,
        'source': source
      })
      if not _DISABLE_CACHING:
        self._case_source_cache[key] = result
      req.response.out.write(result)
      return
    req.response.out.write(self._case_source_cache[key])

  def get_test_case_source(self, req, suite, serial):
    req.response.headers['Content-Type'] = 'text/javascript'
    case = models.Case.lookup(suite, int(serial))
    if not case:
      return self.do_404(req)
    req.response.out.write(case.source)

  def get_test_range_sources(self, req, suite, start, end):
    req.response.headers['Content-Type'] = 'text/javascript'
    key = (suite, start, end)
    if not key in self._chunk_cache:
      chunk = models.Case.lookup_range(suite, int(start), int(end))
      if not chunk:
        return self.do_404(req)
      case_list = [ c.to_json() for c in chunk ]
      json = models.to_json(case_list)
      self._chunk_cache[key] = json
    req.response.out.write(self._chunk_cache[key])

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
      (r'/cases/(\w+)/(\d+)-(\d+).json', dispatcher(sputnik.get_test_range_sources)),
      (r'/suites/(\w+).js', dispatcher(sputnik.get_test_suite_json)),
      (r'/compare/plot.svg', dispatcher(sputnik.get_comparison_plot))
  ], debug=True)


application = initialize_application()


def main():
  run_wsgi_app(application)


if __name__ == "__main__":
  main()
