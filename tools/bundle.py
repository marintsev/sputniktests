#!/usr/bin/python

# Copyright 2010 the Sputnik authors.  All rights reserved.
# This code is governed by the BSD license found in the LICENSE file.

import sys
from tools import bundleutils

# The bundle generator must be imported and called from another script for the
# __module__ name to be set up correctly so the bundle can be read back in.
# Lame.  Python.
if __name__ == '__main__':
  sys.exit(bundleutils.main())
