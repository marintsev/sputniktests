# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

from google.appengine.ext import db
from google.appengine.tools import bulkloader
import logging
import models
import os
import os.path
import re
import sys
import time


_SESSION_ID = 'session_id'


def ignore_path(name):
  return name.startswith('.') or (name == 'CVS')


class AbstractLoader(bulkloader.Loader):

  def __init__(self):
    super(AbstractLoader, self).__init__(self.get_kind(), self.get_properties())
    self.attribs = None
    self.filename = None

  def initialize(self, filename, opts):
    self.filename = filename
    if opts:
      self.attribs = dict([p.split(':') for p in opts.split(',')])
    else:
      self.attribs = {}

  def generate_cases(self):
    count = 0
    limit = self.get_attribute('limit')
    for (root, dirs, files) in os.walk(os.path.join(self.filename, 'tests', 'Conformance')):
      for f in [f for f in dirs if ignore_path(f)]:
        dirs.remove(f)
      for f in files:
        if not f.endswith('.js') or ignore_path(f):
          continue
        yield os.path.join(root, f)
        count = count + 1
        if limit and count >= limit:
          raise StopIteration

  def get_attribute(self, key):
    return self.attribs.get(key, None)

  def get_session_id(self):
    return self.get_attribute(_SESSION_ID)


class SputnikSuiteLoader(AbstractLoader):

  def get_kind(self):
    return 'Suite'

  def get_properties(self):
    return [
      ('family', str),
      ('name', str),
      ('size', int),
      ('upload_session_id', str)
    ]

  def generate_records(self, filename):
    yield ['sputnik', self.get_attribute('name'), 0, self.get_session_id()]


def base64(i):
  if i < _ONE_CHAR_LIMIT:
    return _BASE64[i]
  else:
    return base64(i / _ONE_CHAR_LIMIT) + _BASE64[i % _ONE_CHAR_LIMIT]


_BASE64 = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ$_'
_ONE_CHAR_LIMIT = len(_BASE64)

def snippet_replacer(snippet):
  src = r'(?<![\~\^])(?<!\^.)' + re.escape(snippet)
  return re.compile(src)

class StringSetCompressor(object):
  
  def __init__(self, strings):
    self.strings_ = strings
    self.table_ = []
  
  def run(self, iterations=256, min_snippet_size=5, max_snippet_size=15):
    def print_progress(p):
      if p % 250 == 0:
        sys.stdout.write(str(p / 1000)),
        sys.stdout.flush()
    strings = [s for s in self.strings_]
    old_strings = [s for s in self.strings_]
    total_size_before = sum([len(s) for s in strings])
    last_total_size = total_size_before
    table_size = 0
    for iteration in xrange(0, iterations):
      start = time.time()
      snippet_map = { }
      for index in xrange(0, len(strings)):
        print_progress(index)
        string = strings[index]
        length = len(string)
        i = 0
        i_limit = length - min_snippet_size
        while i <= i_limit:
          j = i + min_snippet_size
          j_limit = min(i + max_snippet_size, length)
          while j <= j_limit:
            snippet = string[i:j]
            old_value = snippet_map.get(snippet)
            if not old_value:
              snippet_map[snippet] = 1
            else:
              snippet_map[snippet] = old_value + 1
            j += 1
          i += 1
      best_savings = 0
      best_snippets = None
      if iteration < _ONE_CHAR_LIMIT:
        marker = '~'
        replacement_length = 2
      else:
        marker = '^'
        replacement_length = 3
      for snippet in snippet_map.keys():
        count = snippet_map[snippet]
        length = len(snippet)
        saved = (length * (count - 1)) - (replacement_length * count)
        if saved >= best_savings:
          if (snippet[-1] == '~') or (snippet[-1] == '^') or (snippet[-2] == '^'):
            continue
        if saved > best_savings:
          best_savings = saved
          best_snippets = []
        if saved >= best_savings:
          best_snippets.append(snippet)
      if best_savings == 0:
        break
      best_snippets.sort()
      best_snippet = best_snippets[0]
      replacement = '%s%s' % (marker, base64(iteration))
      new_total_size = 0
      self.table_.append(best_snippet)
      table_size += len(best_snippet)
      replacer = snippet_replacer(best_snippet)
      for i in xrange(0, len(strings)):
        string = strings[i]
        new_string = replacer.sub(replacement, string)
        new_total_size += len(new_string)
        strings[i] = new_string
      total_savings = last_total_size - new_total_size
      full_size = new_total_size + table_size
      end = time.time()
      print """
Iteration: %(iteration)s
Time: %(time)s
Snippet length: %(snippet_length)s
Snippet:
%(snippet)s
Total savings: %(percent).4g%%
Last savings: %(last_percent).4g%%
String size before: %(total_size_before).1fK
String size after: %(total_size_after).1fK
Table size: %(table_size).1fK
Total size after: %(total_size).1fK
""" % {
        'iteration': iteration,
        'time': time.strftime("%X.", time.gmtime(end - start)) + str(int(((end - start) % 1.0) * 100)),
        'snippet_length': len(best_snippet),
        'snippet': best_snippet,
        'percent': 100 - (100.0 * full_size / total_size_before),
        'last_percent': 100.0 * total_savings / last_total_size,
        'total_size_before': total_size_before / 1024.0,
        'total_size_after': new_total_size / 1024.0,
        'table_size': table_size / 1024.0,
        'total_size': (full_size) / 1024.0
      }
      table_out = open('table', 'w')
      for line in self.table_:
        table_out.write('[%s]\n' % line)
      table_out.close()
      last_total_size = new_total_size
    best_savings = 0
    most_compressed = None
    for i in xrange(0, len(strings)):
      before = old_strings[i]
      after = strings[i]
      diff = len(before) - len(after)
      if diff > best_savings:
        best_savings = diff
        most_compressed = after
    sys.exit(0)


class SputnikCaseLoader(AbstractLoader):

  def __init__(self):
    super(SputnikCaseLoader, self).__init__()
    self.suite_ = None

  def get_kind(self):
    return 'Case'

  def get_suite(self, value):
    if not self.suite_:
      query = db.GqlQuery(
          "SELECT * FROM Suite WHERE upload_session_id = :1 LIMIT 1",
          self.get_session_id()
      )
      self.suite_ = query[0]
    return self.suite_

  def get_properties(self):
    return [
      ('suite', self.get_suite),
      ('name', str),
      ('serial', int),
      ('source', str)
    ]

  def generate_records(self, filename):
    sorted_names = sorted([case for case in self.generate_cases()])
    sources = [open(n).read() for n in sorted_names]
    compressor = StringSetCompressor(sources)
    compressor.run(iterations = 256, min_snippet_size=10, max_snippet_size=25)
    sys.exit(0)

def score(snippet, count):
  return (len(snippet) * (count - 1)) - (2 * count)

loaders = [SputnikSuiteLoader, SputnikCaseLoader]
