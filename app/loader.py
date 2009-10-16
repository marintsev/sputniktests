#!/usr/bin/python
# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


import codecs
import os
import os.path
from google.appengine.tools import bulkloader
import models


def is_hidden(path):
  return path.startswith('.')


def generate_tests(filename, limit):
  count = 0
  for (dirpath, dirnames, filenames) in os.walk(filename):
    for f in [f for f in dirnames if is_hidden(f)]:
      dirnames.remove(f)
    for f in filenames:
      if not f.endswith('.js'):
        continue
      yield os.path.join(dirpath, f)
      count = count + 1
      if limit and count >= limit:
        raise StopIteration


class SputnikLoader(bulkloader.Loader):

  def __init__(self, kind, properties):
    super(SputnikLoader, self).__init__(kind, properties)
    self.attribs = None

  def suite(self):
    return self.attribs['suite']

  def limit(self):
    val = self.attribs.get('limit')
    if val: return int(val)
    else: return None

  def initialize(self, filename, opts):
    self.attribs = dict([p.split(':') for p in opts.split(',')])
    print self.attribs


class CaseLoader(SputnikLoader):

  def __init__(self):
    super(CaseLoader, self).__init__('Case', [
      ('name', str),
      ('suite', str),
      ('source', unicode),
      ('serial', int),
      ('is_negative', bool)
    ])
    self.serial = 0

  def to_test_record(self, filename):
    serial = self.serial
    self.serial += 1
    f = codecs.open(filename, "r", "utf-8")
    contents = f.read()
    f.close()
    name = os.path.basename(filename[:-3])
    is_negative = ('@negative' in contents)
    return [name, self.suite(), contents, serial, str(is_negative)]

  def generate_records(self, filename):
    for case in generate_tests(filename, self.limit()):
      yield self.to_test_record(case)


class SuiteLoader(SputnikLoader):

  def __init__(self):
    super(SuiteLoader, self).__init__('Suite', [
      ('name', str),
      ('count', int)
    ])

  def generate_records(self, filename):
    count = 0
    for case in generate_tests(filename, self.limit()):
      count += 1
    yield [self.suite(), count]


class VersionLoader(SputnikLoader):

  def __init__(self):
    super(VersionLoader, self).__init__('Version', [
      ('current_suite', str)
    ])

  def generate_records(self, filename):
    yield [self.suite()]


loaders = [CaseLoader, SuiteLoader, VersionLoader]
