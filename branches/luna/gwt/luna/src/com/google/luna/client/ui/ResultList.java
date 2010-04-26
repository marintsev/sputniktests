// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.test.ITestCase;

public class ResultList extends Composite implements IResultList {

  interface IMyUiBinder extends UiBinder<Widget, ResultList> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField FlowPanel body;

  public ResultList() {
    initWidget(BINDER.createAndBindUi(this));
  }

  @Override
  public void addResult(ITestCase test, String str) {
    body.insert(new ResultEntry(test, str), 0);
  }

}
