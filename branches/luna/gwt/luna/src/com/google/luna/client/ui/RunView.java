// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class RunView extends PageView implements IRunView {

  interface IMyUiBinder extends UiBinder<Widget, RunView> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField TestControlPanel controls;
  @UiField ResultList results;
  @UiField FlowPanel workspace;

  public RunView() {
    this.initWidget(BINDER.createAndBindUi(this));
  }

  @Override
  public ITestControlPanel getController() {
    return controls;
  }

  @Override
  public IResultList getResults() {
    return this.results;
  }

  @Override
  public void setMode(Mode mode) {
    controls.setMode(mode);
  }

  @Override
  public Element getWorkspace() {
    return workspace.getElement();
  }

}
