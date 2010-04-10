// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;



public interface ITestController {

	public void schedule(ITestRun runner, TestCase testCase);

	public String buildSource(TestCase test);

	public boolean isNegative(TestCase test);

}
