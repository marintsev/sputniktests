# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

from google.appengine.ext import db
from google.appengine.tools import bulkloader
import models
import logging


_SESSION_ID = 'session_id'


class AbstractLoader(bulkloader.Loader):

  def __init__(self):
    super(AbstractLoader, self).__init__(self.get_kind(), self.get_properties())

  def initialize(self, filename, opts):
    if opts:
      self.attribs = dict([p.split(':') for p in opts.split(',')])
    else:
      self.attribs = {}

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
    yield ['sputnik', filename, 0, self.get_session_id()]


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
    for i in xrange(0, 10):
      yield [None, 'name', i, 'source']


loaders = [SputnikSuiteLoader, SputnikCaseLoader]
