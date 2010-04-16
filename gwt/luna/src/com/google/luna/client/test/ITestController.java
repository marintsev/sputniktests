// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;



public interface ITestController {

  public interface Factory {
    public ITestController create();
  }

  public void start(ITestProgressSink runner, TestCase testCase);

  public String buildSource(TestCase test);

  public String buildDescription(TestCase testCase);

  public boolean isNegative(TestCase test);

  public String getLabel(TestCase testCase);

}
