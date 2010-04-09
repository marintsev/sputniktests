// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.user.client.Window;

public class SputnikTestController implements ITestController {

	@Override
	public void run(TestCase testCase) {
		Window.alert(testCase.toString());
	}

}
