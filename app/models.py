#!/usr/bin/python
# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


from google.appengine.ext import db


def to_json(obj):
  t = type(obj)
  if t is dict:
    props = [ ]
    for (key, value) in obj.items():
      props.append('%s:%s' % (key, to_json(value)))
    return '{%s}' % ','.join(props)
  elif t is int:
    return str(obj)
  elif (t is str) or (t is unicode):
    return '"%s"' % obj
  else:
    return str(obj)


class Case(db.Model):

  name = db.StringProperty()
  suite = db.StringProperty()
  source = db.TextProperty()
  serial = db.IntegerProperty()
  is_negative = db.BooleanProperty()

  @staticmethod
  def lookup(suite, serial):
    query = Case.gql('WHERE suite = :1 AND serial = :2', suite, serial)
    return query.get()


class Suite(db.Model):

  name = db.StringProperty()
  count = db.IntegerProperty()

  @staticmethod
  def lookup(suite):
    query = Suite.gql('WHERE name = :1', suite)
    return query.get()

  def to_json(self):
    return to_json({'name': self.name, 'count': self.count})


class Version(db.Model):

  current_suite = db.StringProperty()

  @staticmethod
  def get():
    query = Version.gql("LIMIT 1")
    return query.get()
