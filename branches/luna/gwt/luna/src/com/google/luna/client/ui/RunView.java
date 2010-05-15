// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.Luna;

public class RunView extends PageView implements IRunView {

  public interface Resources extends ClientBundle {

    public interface Css extends Toplevel.Resources.MobileModeCss {
      String outer();
      String controls();
      String results();
      String workspace();
    }

    @Source("RunView.css")
    public Css css();

  }

  private static final Resources RESOURCES = GWT.create(Resources.class);

  public static Resources getResources() {
    return RESOURCES;
  }

  interface IMyUiBinder extends UiBinder<Widget, RunView> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField(provided=true) final Resources resources = getResources();
  @UiField TestControlPanel controls;
  @UiField ResultList results;
  @UiField FlowPanel workspace;

  public RunView() {
    this.initWidget(BINDER.createAndBindUi(this));
    this.addStyleName(Toplevel.getMobileMode(getResources().css()));
    Luna.setWorkspace(workspace.getElement());
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

}