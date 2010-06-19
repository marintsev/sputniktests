// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressBar extends Composite {

  interface IMyUiBinder extends UiBinder<Widget, ProgressBar> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField FlowPanel runBar;
  @UiField FlowPanel loadBar;
  @UiField Label label;

  public ProgressBar() {
    this.initWidget(BINDER.createAndBindUi(this));
    setRunProgress(0);
    setLoadProgress(0);
  }

  public void setRunProgress(double value) {
    label.setText(Math.round(100 * value) + "%");
    runBar.getElement().getStyle().setWidth(100 * value, Style.Unit.PCT);
  }

  public void setLoadProgress(double value) {
    loadBar.getElement().getStyle().setWidth(100 * value, Style.Unit.PCT);
  }

}
