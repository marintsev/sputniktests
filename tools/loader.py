# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

from tools import bundleutils, models
from google.appengine.ext import db
from google.appengine.tools import bulkloader

class SuiteLoader(bulkloader.Loader):
  
  def __init__(self):
    super(SuiteLoader, self).__init__('Suite', [
      ('type', str),
      ('hash', str),
      ('case_count', int)
    ])
  
  def generate_records(self, filename):
    suite = bundleutils.load(filename)
    yield [
      suite.info().type(),
      suite.hash(),
      suite.case_count()
    ]


class CaseLoader(bulkloader.Loader):

  def __init__(self):
    super(CaseLoader, self).__init__('Case', [
      ('suite', self.get_suite),
      ('name', str),
      ('serial', int),
      ('section', str),
      ('source', unicode)
    ])
    self.suite_ = None

  def generate_records(self, filename):
    suite = bundleutils.load(filename)
    for (serial, case) in suite.generate_tests():
      section = '.'.join(case.section())
      source = case.source()
      yield [
        suite.hash(),
        case.name(),
        serial,
        section,
        source
      ]

  def get_suite(self, hash):
    if not self.suite_:
      query = db.GqlQuery("SELECT * FROM Suite WHERE hash = :1 LIMIT 1", hash)
      self.suite_ = query[0]
    return self.suite_


loaders = [SuiteLoader, CaseLoader]
