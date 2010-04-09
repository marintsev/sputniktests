# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


import os
from tools import models
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


class LunaHandler(object):

  def _render(self, path, values):
    return template.render(path, values)

  def _render_template(self, req, path, type, values):
    req.response.headers['Content-Type'] = 'text/html'
    text = self._render(path, values)
    req.response.out.write(text)

  def get_page_renderer(self, page, is_mobile=False):
    def render(req):
      path = os.path.join('gwt', 'luna', 'war', 'Luna.html')
      if is_mobile:
        relative_path = '../'
      else:
        relative_path = ''
      self._render_template(req, path, 'text/html', {
        'page': page,
        'version': '1',
        'is_server_side_devel': int(False),
        'active_package': to_json(self.get_active_package_object()),
        'data_path': '../../data/',
        'relative_path': relative_path
      })
    return render
  
  def _emit_json(self, req, obj):
    text = to_json(obj)
    req.response.headers['Content-Type'] = 'text/javascript'
    callback = req.request.params.get('callback', None)
    if callback:
      req.response.out.write('%s(%s)' % (callback, text))
    else:
      req.response.out.write(text)
  
  def get_suite(self, req):
    self._emit_json(req, models.Suite.all()[0])
  
  def get_active_package_object(self):
    return {
      's': [s for s in models.Suite.all()],
      'v': '0'
    }
  
  def get_active_package(self, req):
    self._emit_json(req, self.get_active_package_object())

  def get_cases(self, req):
    start = int(req.request.params.get('from', '0'))
    end = int(req.request.params.get('to', '0'))
    suite = models.Key.from_string(req.request.params.get('suite', None))
    query = models.Case.gql(
        'WHERE suite = :1 AND serial >= :2 AND serial < :3 ORDER BY serial',
        suite, start, end)
    result = [c for c in query]
    self._emit_json(req, {
      's': start,
      'e': end,
      'c': result
    })


def dispatcher(method, cache=None):
  class Dispatcher(webapp.RequestHandler):
    def get(self, *args):
      return method.__call__(self, *args)
  Dispatcher.__name__ = method.__name__
  return Dispatcher


def initialize_application():
  handler = LunaHandler()
  return webapp.WSGIApplication([
    (r'/data/suite.json', dispatcher(handler.get_suite)),
    (r'/data/cases.json', dispatcher(handler.get_cases)),
    (r'/data/activePackage.json', dispatcher(handler.get_active_package)),
    (r'/(?:about.html)?', dispatcher(handler.get_page_renderer('About'))),
    (r'/run.html', dispatcher(handler.get_page_renderer('Run'))),
    (r'/compare.html', dispatcher(handler.get_page_renderer('Compare'))),
    (r'/m/(?:about.html)?', dispatcher(handler.get_page_renderer('About', is_mobile=True))),
    (r'/m/run.html', dispatcher(handler.get_page_renderer('Run', is_mobile=True))),
    (r'/m/compare.html', dispatcher(handler.get_page_renderer('Compare', is_mobile=True))),
  ], debug=True)

application = initialize_application()

def main():
  run_wsgi_app(application)

if __name__ == "__main__":
  main()
