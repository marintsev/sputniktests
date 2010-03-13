# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

import models
import os
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app

_ESCAPEES = {
  '"': '\\"',
  '\\': '\\\\',
  '\b': '\\b',
  '\f': '\\f',
  '\n': '\\n',
  '\r': '\\r',
  '\t': '\\t'
}

def json_escape(s):
  result = []
  for c in s:
    escapee = _ESCAPEES.get(c, None)
    if escapee:
      result.append(escapee)
    elif c < ' ':
      result.append("\\u%.4X" % ord(c))
    else:
      result.append(c)
  return "".join(result)

def to_json(obj):
  t = type(obj)
  if t is dict:
    props = [ ]
    for (key, value) in obj.items():
      props.append('"%s":%s' % (json_escape(key), to_json(value)))
    return '{%s}' % ','.join(props)
  elif (t is int) or (t is long):
    return str(obj)
  elif (t is str) or (t is unicode):
    return '"%s"' % json_escape(obj)
  elif (t is list):
    return '[%s]' % ','.join([to_json(o) for o in obj])
  elif t is bool:
    if obj: return '1'
    else: return '0'
  else:
    return to_json(obj.to_json())

def new_case_block(start, end, values):
  return { 's': start, 'e': end, 'v': values }

def new_case(serial):
  return { 'i': serial, 'n': False }

class Sputnik(object):

  def __init__(self):
    pass

  def get_case_block(self, req):
    start = int(req.request.params.get('from', '0'))
    end = int(req.request.params.get('to', '0'))
    suite = models.Key.from_string(req.request.params.get('suite', ''))
    query = models.Case.gql(
        'WHERE suite = :1 AND serial >= :2 AND serial < :3',
        suite, start, end)
    result = []
    for case in query:
      result.append(case)
    req.response.out.write(to_json(result))

  def get_suite(self, req):
    family = req.request.params.get('family', '')
    name = req.request.params.get('name', '')
    query = models.Suite.gql(
        'WHERE family = :1 AND name = :2 ORDER BY created DESC LIMIT 1',
        family, name)
    for suite in query:
      req.response.out.write(to_json(suite))
      return

  def get_suites(self, req):
    result = [s for s in models.Suite.all()]
    req.response.out.write(to_json(result))

  def _render(self, path, values):
    p = os.path.join(os.path.dirname(__file__), path)
    return template.render(p, values)

  def _render_template(self, req, path, type, values):
    req.response.headers['Content-Type'] = 'text/html'
    text = self._render(path, values)
    req.response.out.write(text)

  def get_page_renderer(self, name):
    def render(req):
      path = os.path.join('dynamic', 'sputnik.html')
      self._render_template(req, path, 'text/html', {
        'name': name,
        'version': '1'
      })
    return render

def dispatcher(method, cache=None):
  class Dispatcher(webapp.RequestHandler):
    def get(self, *args):
      return method.__call__(self, *args)
  Dispatcher.__name__ = method.__name__
  return Dispatcher

def initialize_application():
  sputnik = Sputnik()
  return webapp.WSGIApplication([
    (r'/data/cases.json', dispatcher(sputnik.get_case_block)),
    (r'/data/suite.json', dispatcher(sputnik.get_suite)),
    (r'/(?:index.html)?', dispatcher(sputnik.get_page_renderer('Index'))),
    (r'/inspect.html', dispatcher(sputnik.get_page_renderer('Inspect'))),
    (r'/inspect/suites.json', dispatcher(sputnik.get_suites))
  ], debug=True)

application = initialize_application()

def main():
  run_wsgi_app(application)

if __name__ == "__main__":
  main()
