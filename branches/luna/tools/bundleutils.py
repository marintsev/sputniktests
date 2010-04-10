#!/usr/bin/python

# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

import codecs
import time
import hashlib
import pickle
import os
import re
import sys
from os.path import exists, join


class TestCase(object):
  
  # Args:
  #   filename: the name of the file containing this test
  #   name: the name of the test within its section.  For instance 'A1 T3' for
  #     the third test of the first assertion within that section.  This is not
  #     meant to be a human readable description of the test.
  #   section: a list of strings giving the path of the section this test
  #     relates to.  For instance, ['15', '9', '5', '9'] for a test relating to
  #     section 15.9.5.9, Date.prototype.getTime.
  #   source: the source code of this test as it will be served to the app
  #   extra: a map of extra attributes that will be passed on to the app
  def __init__(self, filename, name, section, source, extra=None):
    self.filename_ = filename
    self.name_ = name
    self.section_ = section
    self.source_ = source
    self.extra_ = extra
  
  def name(self):
    return self.name_
  
  def section(self):
    return self.section_
  
  def source(self):
    return self.source_
  
  def add_to_hash(self, m):
    m.update(self.name_)
    m.update('.'.join(self.section_))
    m.update(self.source_.encode('utf-8'))
  
  def __str__(self):
    return 'case { file: %s, name: %s, section: %s }' % (self.filename_, self.name_, str(self.section_))


class Section(object):
  
  # Args:
  #   index: the index of this section, for instance '9'.
  #   parent: a list of strings giving the path to the parent of this section,
  #     for instance ['15', '9', '5']
  #   title: the human-readable title of this section, for instance
  #     'Data.prototype.getTime'.
  def __init__(self, index, parent, title):
    self.index_ = index
    self.parent_ = parent
    self.title_ = title
  
  def add_to_hash(self, m):
    m.update(self.index_)
    m.update('.'.join(self.parent_))
    if self.title_:
      m.update(self.title_)
  
  def full_path(self):
    return tuple(self.parent_ + [self.index_])
  
  def parent(self):
    return self.parent_
  
  def __str__(self):
    return 'section { index: %s, parent: %s, title: "%s" }' % (self.index_, str(self.parent_), self.title_)


def log(str):
  print str


def load(name):
  o = open(name, 'r')
  try:
    result = pickle.load(o)
  finally:
    o.close()
  return result


class TestSuite(object):
  
  def __init__(self, info, sections, cases):
    self.info_ = info
    self.sections_ = sections
    self.cases_ = cases
    self.hash_ = self.calculate_hash()
  
  def hash(self):
    return self.hash_
  
  def info(self):
    return self.info_
  
  def case_count(self):
    size = 0
    for (serial, case) in self.generate_tests():
      size += 1
    return size
  
  def generate_tests(self):
    def compare_strings(a, b):
      return int(a) - int(b)
    def compare_sections(a, b):
      lim = min(len(a), len(b))
      for i in xrange(0, lim):
        if a[i] != b[i]:
          return compare_strings(a[i], b[i])
      if len(a) != len(b):
        return len(a) - len(b)
      return 0
    serial = 0
    for section in sorted(self.cases_.keys(), compare_sections):
      cases = self.cases_[section]
      for name in sorted(cases.keys()):
        yield (serial, cases[name])
        serial += 1
  
  def calculate_hash(self):
    log('Calculating hash')
    m = hashlib.md5()
    m.update(str(time.time()))
    # Add sections to hash
    for section in sorted(self.sections_.keys()):
      self.sections_[section].add_to_hash(m)
    for section in sorted(self.cases_.keys()):
      cases = self.cases_[section]
      for case in sorted(cases.keys()):
        cases[case].add_to_hash(m)
    result = m.hexdigest()
    log('Hash: %s' % result)
    return result


# Extra information about a test suite that can be used by the app to determine
# how to handle the tests.
class TestSuiteInfo(object):
  
  def __init__(self, type):
    self.type_ = type
  
  def type(self):
    return self.type_


class Bundler(object):
  
  def __init__(self, root):
    self.root_ = root
  
  def run(self, output):
    o = open(output, 'w')
    bundle = self.build_bundle()
    log('Writing bundle to %s' % output)
    try:
      pickle.dump(bundle, o)
    finally:
      o.close()
    log('Done writing bundle')
  
  def build_bundle(self):
    # Preliminary checks
    if not self._verify_root():
      print '%s does not look like a valid test suite.' % self.root_
      return 1
    log('Creating section structure')
    section_map = {}
    for section in self.test_sections(self.root_):
      key = section.full_path()
      if key in section_map:
        print 'Duplicate section "%s"' % str(section)
        return 1
      section_map[key] = section
    # Verify that the section structure is sound
    for section in section_map.values():
      parent = section.parent()
      if len(parent) == 0:
        continue
      if not tuple(parent) in section_map:
        print 'Parent section %s doesn\'t exist' % str(parent)
        return 1
    # Traverse directory structure and create a list of all test files
    log('Traversing test files')
    test_files = []
    for test_file in self.test_files(self.root_):
      test_files.append(test_file)
    # Read test files
    log('Building test cases')
    cases_by_section = { }
    for test_file in test_files:
      o = codecs.open(test_file, 'r', 'utf-8')
      try:
        source = o.read()
      finally:
        o.close()
      test_case = self.make_test_case(test_file, source)
      section = tuple(test_case.section())
      if not section in section_map:
        print 'Test %s is in non-existing section %s' % (str(test_case), str(section))
        return 1
      if not section in cases_by_section:
        cases_by_section[section] = { }
      section_cases = cases_by_section[section]
      if test_case.name() in section_cases:
        print 'Section %s already has a test case %s' % (str(section), test_case.name())
        return 1
      section_cases[test_case.name()] = test_case
    info = self.get_info()
    return TestSuite(info, section_map, cases_by_section)

  def _verify_root(self):
    for dirs in self.get_expected_directories():
      path = join(self.root_, dirs)
      if not exists(path):
        return False
    return True

  # Returns a list of paths, relative to the test suite root directory, that
  # must exist for this to be a valid test suite directory.  All directories
  # that must be present for the bundler to work must be listed here.
  def get_expected_directories(self):
    raise NotImplemented
  
  # Returns a generator generating all names of files containing test cases.
  def test_files(self, root):
    raise NotImplemented
  
  # Returns a generator generating Section objects for all sections in this
  # test suite.
  def test_sections(self, root):
    raise NotImplemented

  # Given a test filename and its source, returns the TestCase object to be
  # stored in the test case database
  def make_test_case(self, name, source):
    raise NotImplemented
  
  # Destructively removes hidden directories from a list of directory names.
  def remove_hidden_directories(self, dirs):
    def is_hidden(dir):
      return dir.startswith('.')
    for dir in [d for d in dirs if is_hidden(d)]:
      dirs.remove(dir)
  
  # Returns a generator generating all unhidden files under the specified
  # directory.
  def unhidden_files(self, path):
    for (root, dirs, files) in os.walk(path):
      self.remove_hidden_directories(dirs)
      yield (root, dirs, files)


class SputnikBundler(Bundler):

  def get_expected_directories(self):
    return ['lib', 'tests', join('tests', 'Conformance')]
  
  def test_files(self, root):
    tests_root = join(root, 'tests', 'Conformance')
    for (root, dirs, files) in self.unhidden_files(tests_root):
      for file in files:
        if file.endswith('.js'):
          yield join(root, file)
  
  def split_test_filename(self, filename):
    assert filename.endswith('.js')
    assert filename.startswith('S')
    base_name = filename[1:-3]
    first_sep = base_name.index('_')
    section = [str(int(p)) for p in base_name[:first_sep].split('.')]
    id = base_name[first_sep+1:]
    return (section, id)
  
  def test_sections(self, root):
    tests_root = join(root, 'tests', 'Conformance')
    # Sections based on directories
    directory_sections = {}
    # Sections inferred from test files
    extra_sections = set()
    for (root, dirs, files) in self.unhidden_files(tests_root):
      path_parts = self.relative_path_parts(root)
      for part in path_parts:
        try:
          first_sep = part.index('_')
        except ValueError:
          continue
        section = [int(p) for p in part[:first_sep].split('.')]
        name = part[first_sep+1:]
        directory_sections[tuple(section)] = name
        for file in files:
          if file.endswith('.js'):
            (file_section, file_id) = self.split_test_filename(file)
            extra_sections.add(tuple([int(p) for p in file_section]))
    # First yield all "proper" sections found from the directory structure
    for (section, title) in directory_sections.items():
      name = str(section[-1])
      parent = [str(p) for p in section[:-1]]
      yield Section(name, parent, title.replace('_', ' '))
    # Then generate the extra ones that weren't present as directories but
    # could be inferred from file names
    for extra_section in extra_sections:
      if not extra_section in directory_sections:
        name = str(extra_section[-1])
        parent = [str(s) for s in extra_section[:-1]]
        yield Section(name, parent, None)
  
  def relative_path_parts(self, name):
    tests_root = join(self.root_, 'tests', 'Conformance')
    relative_path = name[len(tests_root):]
    return [p for p in relative_path.split(os.path.sep) if p]
  
  def make_test_case(self, file, source):
    relative_path = self.relative_path_parts(file)
    filename = relative_path[-1]
    (section, id) = self.split_test_filename(filename)
    return TestCase(file, id, section, source)
  
  def get_info(self):
    return TestSuiteInfo('sputnik')


class Es5ConformBundler(Bundler):
  pass


def build_option_parser():
  import optparse
  parser = optparse.OptionParser()
  types = '|'.join(sorted(_BUNDLERS.keys()))
  parser.add_option('-t', '--type', help='Type of test suite to bundle [%s]' % types)
  parser.add_option('-r', '--root', help='Root directory of test suite to bundle')
  parser.add_option('-o', '--output', help='Output file')
  return parser

_BUNDLERS = {
  'sputnik': SputnikBundler,
  'es5conform': Es5ConformBundler
}

def main():
  parser = build_option_parser()
  (options, args) = parser.parse_args()
  if not options.root:
    print 'A --root directory must be specified.'
    parser.print_help()
    return 1
  if not exists(options.root):
    print 'Root directory "%s" doesn\'t exist.' % options.root
    return 1
  if not options.type:
    print 'A --type must be specified.'
    parser.print_help()
    return 1
  if not options.type in _BUNDLERS.keys():
    print 'Invalid type "%s".' % options.type
    parser.print_help()
    return 1
  if not options.output:
    print 'No --output file specified'
    parser.print_help()
    return 1
  bundler_factory = _BUNDLERS[options.type]
  bundler = bundler_factory(options.root)
  return bundler.run(options.output)

if __name__ == '__main__':
  print 'This script must be called through bundle.py'
  sys.exit(1)
