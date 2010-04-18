# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

from google.appengine.ext import db


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
      'i': str(self.key_.id()),
      'k': self.key_.kind()
    }


class Suite(db.Model):

  type = db.StringProperty()
  hash = db.StringProperty()
  case_count = db.IntegerProperty()
  created = db.DateTimeProperty(auto_now_add=True)

  def to_json(self):
    return {
      't': self.type,
      'c': self.case_count,
      'k': Key(self.key())
    }


class Case(db.Model):

  suite = db.ReferenceProperty(Suite)
  name = db.StringProperty()
  serial = db.IntegerProperty()
  section = db.StringProperty()
  source = db.TextProperty()

  def to_json(self):
    return {
      'n': self.name,
      's': self.section,
      'c': unicode(self.source)
    }


def user_to_json(u):
  return {
    'n': u.nickname(),
    'e': u.email(),
    'i': u.user_id()
  }


def user_info_to_json(is_manager, user):
  return {
    'u': user_to_json(user),
    'm': is_manager
  }


class UserInfo(db.Model):

  user = db.UserProperty()
  is_manager = db.BooleanProperty()

  def to_json(self):
    return user_info_to_json(self.is_manager, self.user)
