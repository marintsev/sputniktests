// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.logic.ITestControlPanelView;
import com.google.luna.client.utils.Promise;

public class TestControlPanelWidget extends Composite implements ITestControlPanelView {

  public interface Resources extends ClientBundle {

    public interface Css extends ToplevelWidget.Resources.MobileModeCss {
      String outer();
      String progressRow();
      String progressCol();
      String progress();
      String controlButton();
      String stats();
      String caption();
      String value();
    }

    @Source("TestControlPanelWidget.css")
    public Css css();

  }

  private static final Resources RESOURCES = GWT.create(Resources.class);

  public static Resources getResources() {
    return RESOURCES;
  }

  interface IMyUiBinder extends UiBinder<Widget, TestControlPanelWidget> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField(provided=true) final Resources resources = getResources();
  @UiField ProgressBar progress;
  @UiField Button reset;
  @UiField Button start;
  @UiField PromiseLabel current;
  @UiField Label total;
  @UiField Label succeeded;
  @UiField Label failed;

  public TestControlPanelWidget() {
    this.initWidget(BINDER.createAndBindUi(this));
    this.addStyleName(ToplevelWidget.getMobileMode(getResources().css()));
  }

  public void init(final IHandler handler) {
    start.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        handler.onStartClicked();
      }
    });
    reset.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        handler.onResetClicked();
      }
    });
  }

  @Override
  public void updateStats(Promise<String> testName, int totalCount,
      int succeededCount, int failedCount) {
    current.setText(testName);
    total.setText(Integer.toString(totalCount));
    succeeded.setText(Integer.toString(succeededCount));
    failed.setText(Integer.toString(failedCount));
  }

  @Override
  public void setRunProgress(double value) {
    progress.setRunProgress(value);
  }

  @Override
  public void setLoadProgress(double value) {
    progress.setLoadProgress(value);
  }

  @Override
  public void updateReset(String label, boolean enabled) {
    reset.setText(label);
    reset.setEnabled(enabled);
  }

  @Override
  public void updateStart(String label, boolean enabled) {
    start.setText(label);
    start.setEnabled(enabled);
  }

}
