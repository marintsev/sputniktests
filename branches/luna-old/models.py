# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

from google.appengine.ext import db


class Family(object):
  pass


class Sputnik(Family):
  pass


class Es5Conform(Family):
  pass


FAMILIES = {
  'sputnik': Sputnik(),
  'es5conform': Es5Conform()
}


class Key(object):

  def __init__(self, key):
    self.key_ = key

  @staticmethod
  def from_string(str):
    dot = str.index('.')
    kind = str[:dot]
    index = int(str[dot+1:])
    return db.Key.from_path(kind, index)

  def to_json(self):
    return {
      'i': self.key_.id(),
      'k': self.key_.kind()
    }

class Suite(db.Model):

  family = db.StringProperty()
  name = db.StringProperty()
  size = db.IntegerProperty()
  upload_session_id = db.StringProperty()
  created = db.DateTimeProperty(auto_now_add=True)

  def to_json(self):
    return {
        'f': self.family,
        'n': self.name,
        's': self.size,
        'k': Key(self.key())
    }


class Case(db.Expando):

  suite = db.ReferenceProperty(Suite)
  name = db.StringProperty()
  serial = db.IntegerProperty()
  source = db.TextProperty()

  def to_json(self):
    return {
      'n': self.name
    }
