#!/usr/bin/python
# Copyright 2009 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.


import logging
import optparse
import os
from os import path
import platform
import re
import subprocess
import sys
import tempfile
import time


# logging.getLogger().setLevel(logging.INFO)


class SputnikError(Exception):

  def __init__(self, message):
    self.message = message


def ReportError(s):
  raise SputnikError(s)


def BuildOptions():
  result = optparse.OptionParser()
  result.add_option("--command", default=None, help="The command-line to run")
  result.add_option("--tests", default=path.abspath('.'), help="Path to the tests")
  result.add_option("--cat", default=False, action="store_true")
  return result


def ValidateOptions(options):
  if not options.command:
    ReportError("A --command must be specified.")
  if not path.exists(options.tests):
    ReportError("Couldn't find test path '%s'" % options.tests)


_PLACEHOLDER_PATTERN = re.compile(r"\{\{(\w+)\}\}")
_INCLUDE_PATTERN = re.compile(r"\$INCLUDE\(\"(.*)\"\);")
_SPECIAL_CALL_PATTERN = re.compile(r"\$([A-Z]+)(?=\()")


_SPECIAL_CALLS = {
  'ERROR': 'testFailed',
  'PRINT': 'testPrint'
}


def IsWindows():
  p = platform.system()
  return (p == 'Windows') or (p == 'Microsoft')


def StripHeader(str):
  while str.startswith('//') and "\n" in str:
    str = str[str.index("\n")+1:]
  return str.lstrip()


class TempFile(object):

  def __init__(self, suffix="", prefix="tmp", text=False):
    self.suffix = suffix
    self.prefix = prefix
    self.text = text
    self.fd = None
    self.name = None
    self.is_closed = False
    self.Open()

  def Open(self):
    (self.fd, self.name) = tempfile.mkstemp(
        suffix = self.suffix,
        prefix = self.prefix,
        text = self.text
    )

  def Write(self, str):
    os.write(self.fd, str)

  def Read(self):
    f = file(self.name)
    result = f.read()
    f.close()
    return result

  def Close(self):
    if not self.is_closed:
      self.is_closed = True
      os.close(self.fd)

  def Dispose(self):
    try:
      self.Close()
      os.unlink(self.name)
    except OSError, e:
      logging.error("Error disposing temp file: %s", str(e))


class TestResult(object):

  def __init__(self, exit_code, stdout, stderr, case):
    self.exit_code = exit_code
    self.stdout = stdout
    self.stderr = stderr
    self.case = case

  def ReportOutcome(self):
    if self.HasUnexpectedOutcome():
      print "=== %s failed ===" % self.case.GetName()
      out = self.stdout.strip()
      if len(out) > 0:
        print "--- standard output ---"
        print out
      err = self.stderr.strip()
      if len(err) > 0:
        print "--- standard error ---"
        print err
      print "==="
    else:
      print "%s passed" % self.case.GetName()

  def HasFailed(self):
    return self.exit_code != 0

  def HasUnexpectedOutcome(self):
    return self.HasFailed() and not self.case.IsNegative()


class TestCase(object):

  def __init__(self, suite, name, full_path):
    self.suite = suite
    self.name = name
    self.full_path = full_path
    self.contents = None
    self.is_negative = None

  def GetName(self):
    return path.join(*self.name)

  def GetRawContents(self):
    if self.contents is None:
      f = open(self.full_path)
      self.contents = f.read()
      f.close()
    return self.contents

  def IsNegative(self):
    if self.is_negative is None:
      self.is_negative = ("@negative" in self.GetRawContents())
    return self.is_negative

  def GetSource(self):
    source = self.suite.GetInclude("framework.js", False)
    source += StripHeader(self.GetRawContents())
    def IncludeFile(match):
      return self.suite.GetInclude(match.group(1))
    source = _INCLUDE_PATTERN.sub(IncludeFile, source)
    def SpecialCall(match):
      key = match.group(1)
      return _SPECIAL_CALLS.get(key, match.group(0))
    source = _SPECIAL_CALL_PATTERN.sub(SpecialCall, source)
    return source

  def InstantiateTemplate(self, template, params):
    def GetParameter(match):
      key = match.group(1)
      return params.get(key, match.group(0))
    return _PLACEHOLDER_PATTERN.sub(GetParameter, template)

  def RunTestIn(self, command_template, tmp):
    tmp.Write(self.GetSource())
    tmp.Close()
    command = self.InstantiateTemplate(command_template, {
      'path': tmp.name
    })
    (code, out, err) = self.Execute(command)
    return TestResult(code, out, err, self)

  def Execute(self, command):
    if IsWindows():
      args = '"%s"' % command
    else:
      args = command.split(" ")
    stdout = TempFile(prefix="sputnik-out-")
    stderr = TempFile(prefix="sputnik-err-")
    try:
      logging.info("exec: %s", str(args))
      process = subprocess.Popen(
        args,
        shell = IsWindows(),
        stdout = stdout.fd,
        stderr = stderr.fd
      )
      code = process.wait()
      out = stdout.Read()
      err = stderr.Read()
    finally:
      stdout.Dispose()
      stderr.Dispose()
    return (code, out, err)

  def Run(self, command_template):
    tmp = TempFile(suffix=".js", prefix="sputnik-", text=True)
    try:
      result = self.RunTestIn(command_template, tmp)
    finally:
      tmp.Dispose()
    return result

  def Print(self):
    print self.GetSource()


class ProgressIndicator(object):

  def __init__(self, count):
    self.count = count
    self.succeeded = 0
    self.failed = 0

  def HasRun(self, result):
    result.ReportOutcome()
    if result.HasUnexpectedOutcome():
      self.failed += 1
    else:
      self.succeeded += 1


class TestSuite(object):

  def __init__(self, root):
    self.test_root = path.join(root, 'tests', 'Conformance')
    self.lib_root = path.join(root, 'lib')
    self.include_cache = { }

  def Validate(self):
    if not path.exists(self.test_root):
      ReportError("No test repository found")
    if not path.exists(self.lib_root):
      ReportError("No test library found")

  def IsHidden(self, path):
    return path.startswith('.') or path == 'CVS'

  def IsTestCase(self, path):
    return path.endswith('.js')

  def ShouldRun(self, rel_path, tests):
    if len(tests) == 0:
      return True
    for test in tests:
      if test in rel_path:
        return True
    return False

  def GetSpecialInclude(self, name):
    if name == "environment.js":
      dst_attribs = GetDaylightSavingsAttribs()
      if not dst_attribs:
        return None
      lines = []
      for key in sorted(dst_attribs.keys()):
        lines.append('var $DST_%s = %s;' % (key, str(dst_attribs[key])))
      return "\n".join(lines)
    else:
      return None

  def GetInclude(self, name, strip_header=True):
    if not name in self.include_cache:
      value = self.GetSpecialInclude(name)
      if value:
        self.include_cache[name] = value
      else:
        static = path.join(self.lib_root, name)
        if path.exists(static):
          f = open(static)
          contents = f.read()
          if strip_header:
            contents = StripHeader(contents)
          self.include_cache[name] = contents
          f.close()
        else:
         self.include_cache[name] = ""
    return self.include_cache[name]

  def EnumerateTests(self, tests):
    logging.info("Listing tests in %s", self.test_root)
    cases = []
    for root, dirs, files in os.walk(self.test_root):
      for f in [x for x in dirs if self.IsHidden(x)]:
        dirs.remove(f)
      dirs.sort()
      for f in sorted(files):
        if self.IsTestCase(f):
          full_path = path.join(root, f)
          if full_path.startswith(self.test_root):
            rel_path = full_path[len(self.test_root)+1:]
          else:
            logging.warning("Unexpected path %s", full_path)
            rel_path = full_path
          if self.ShouldRun(rel_path, tests):
            basename = path.basename(full_path)[:-3]
            name = rel_path.split(path.sep)[:-1] + [basename]
            cases.append(TestCase(self, name, full_path))
    logging.info("Done listing tests")
    return cases

  def Run(self, command_template, tests):
    if not "{{path}}" in command_template:
      command_template += " {{path}}"
    cases = self.EnumerateTests(tests)
    if len(cases) == 0:
      ReportError("No tests to run")
    progress = ProgressIndicator(len(cases))
    for case in cases:
      result = case.Run(command_template)
      progress.HasRun(result)

  def Print(self, tests):
    cases = self.EnumerateTests(tests)
    if len(cases) > 0:
      cases[0].Print()


def GetDaylightSavingsTimes():
  # Is the given floating-point time in DST?
  def IsDst(t):
    return time.localtime(t)[-1]
  # Binary search to find an interval between the two times no greater than
  # delta where DST switches, returning the midpoint.
  def FindBetween(start, end, delta):
    while end - start > delta:
      middle = (end + start) / 2
      if IsDst(middle) == IsDst(start):
        start = middle
      else:
        end = middle
    return (start + end) / 2
  now = time.time()
  one_month = (30 * 24 * 60 * 60)
  # First find a date with different daylight savings.  To avoid corner cases
  # we try four months before and after today.
  after = now + 4 * one_month
  before = now - 4 * one_month
  if IsDst(now) == IsDst(before) and IsDst(now) == IsDst(after):
    logger.warning("Was unable to determine DST info.")
    return None
  # Determine when the change occurs between now and the date we just found
  # in a different DST.
  if IsDst(now) != IsDst(before):
    first = FindBetween(before, now, 1)
  else:
    first = FindBetween(now, after, 1)
  # Determine when the change occurs between three and nine months from the
  # first.
  second = FindBetween(first + 3 * one_month, first + 9 * one_month, 1)
  # Find out which switch is into and which if out of DST
  if IsDst(first - 1) and not IsDst(first + 1):
    start = second
    end = first
  else:
    start = first
    end = second
  return (start, end)


def GetDaylightSavingsAttribs():
  times = GetDaylightSavingsTimes()
  if not times:
    return None
  (start, end) = times
  def DstMonth(t):
    return time.localtime(t)[1] - 1
  def DstHour(t):
    return time.localtime(t - 1)[3] + 1
  def DstSunday(t):
    if time.localtime(t)[2] > 15:
      return "'last'"
    else:
      return "'first'"
  def DstMinutes(t):
    return (time.localtime(t - 1)[4] + 1) % 60
  attribs = { }
  attribs['start_month'] = DstMonth(start)
  attribs['end_month'] = DstMonth(end)
  attribs['start_sunday'] = DstSunday(start)
  attribs['end_sunday'] = DstSunday(end)
  attribs['start_hour'] = DstHour(start)
  attribs['end_hour'] = DstHour(end)
  attribs['start_minutes'] = DstMinutes(start)
  attribs['end_minutes'] = DstMinutes(end)
  return attribs


def Main():
  parser = BuildOptions()
  (options, args) = parser.parse_args()
  ValidateOptions(options)
  test_suite = TestSuite(options.tests)
  test_suite.Validate()
  if options.cat:
    test_suite.Print(args)
  else:
    test_suite.Run(options.command, args)


if __name__ == '__main__':
  try:
    Main()
    sys.exit(0)
  except SputnikError, e:
    print "Error: %s" % e.message
    sys.exit(1)
