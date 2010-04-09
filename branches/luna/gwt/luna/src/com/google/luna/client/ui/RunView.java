// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class RunView extends PageView implements IRunView {

  interface IMyUiBinder extends UiBinder<Widget, RunView> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

	@UiField TestControlPanel controls;

	public RunView() {
		this.initWidget(BINDER.createAndBindUi(this));
	}

	@Override
	public ITestControlPanel getController() {
		return controls;
	}

	@Override
	public void setMode(Mode mode) {
		controls.setMode(mode);
	}

}
