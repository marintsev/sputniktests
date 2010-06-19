// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ErrorDialogBody extends Composite {

  interface IMyUiBinder extends UiBinder<Widget, ErrorDialogBody> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField Label title;
  @UiField Panel frames;

  public ErrorDialogBody(Throwable error) {
  	this.initWidget(BINDER.createAndBindUi(this));
  	title.setText(error.toString());
  	for (StackTraceElement frame : error.getStackTrace()) {
  		frames.add(new Label(frame.toString()));
  	}
	}

}
