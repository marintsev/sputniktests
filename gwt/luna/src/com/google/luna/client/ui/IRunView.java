// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.dom.client.Element;


public interface IRunView extends IPageView {

	public enum Mode {
		DISABLED, READY, RUNNING, PAUSED
	}

	public ITestControlPanel getController();

	public void setMode(Mode mode);

	public Element getWorkspace();

}
