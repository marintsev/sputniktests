#!/usr/bin/python
# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


from google.appengine.ext import db


def json_escape(s):
  result = ''
  for c in s:
    if c == '"':
      result += '\\"'
    elif c == '\\':
      result += '\\\\'
    elif c == '\b':
      result += '\\b'
    elif c == '\f':
      result += '\\f'
    elif c == '\n':
      result += '\\n'
    elif c == '\r':
      result += '\\r'
    elif c == '\t':
      result += '\\t'
    elif c < ' ':
      result += "\\u%.4X" % ord(c)
    else:
      result += c
  return result


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
    if t: return '1'
    else: return '0'
  else:
    return to_json(obj.to_json())


class Case(db.Model):

  name = db.StringProperty()
  suite = db.StringProperty()
  source = db.TextProperty()
  serial = db.IntegerProperty()
  is_negative = db.BooleanProperty()

  def to_json(self):
    return {'name': self.name,
            'isNegative': self.is_negative,
            'source': unicode(self.source)}

  @staticmethod
  def lookup(suite, serial):
    query = Case.gql('WHERE suite = :1 AND serial = :2', suite, serial)
    return query.get()

  @staticmethod
  def lookup_range(suite, start, end):
    query = Case.gql('WHERE suite = :1 AND serial >= :2 AND serial < :3', suite, start, end)
    return query.fetch(end - start)


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
