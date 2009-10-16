#!/usr/bin/python
# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


import models
import os.path
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


class Sputnik(object):

  def __init__(self):
    self._case_source_cache = RandomCache(_CASE_CACHE_SIZE)
    self._plot_cache = RandomCache(_PLOT_CACHE_SIZE)

  def do_404(self, req):
    req.error(404)

  def do_500(self, req, message):
    req.response.out.write(message)
    req.error(500)

  def get_template(self, name, attribs):
    path = os.path.join(os.path.dirname(__file__), 'dynamic', name)
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

  def expand_matrix(self, m):
    raw_matrix = [s.split(',') for s in m.split(':')]
    headers = raw_matrix[0]
    rows = [[int(i) for i in r] for r in raw_matrix[1:]]
    points = []
    for x in xrange(5, 100, 10):
      for y in xrange(5, 100, 10):
        points.append({'x': x, 'y': y})
    return points

  def get_comparison_plot(self, req):
    req.response.headers['Content-Type'] = 'image/svg+xml'
    matrix_param = req.request.params.get('m', None)
    if matrix_param:
      key = matrix_param
      matrix = self.expand_matrix(matrix_param)
      draw_points = True
    else:
      key = 'none'
      matrix = ''
      draw_points = False
    #if not key in self._plot_cache:
    plot = self.get_template('plot.svg', {
      'draw_points': draw_points,
      'points': matrix
    })
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
