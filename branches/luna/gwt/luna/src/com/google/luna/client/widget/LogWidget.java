// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.Log;
import com.google.luna.client.logic.ILogView;

public class LogWidget extends Composite implements ILogView {

  interface IMyUiBinder extends UiBinder<Widget, LogWidget> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField FlowPanel entries;

  public LogWidget() {
    this.initWidget(BINDER.createAndBindUi(this));
  }

  @Override
  public void addMessage(Log.Component component, String message) {
    entries.insert(new Label(component + ": " + message), 0);
  }

}
