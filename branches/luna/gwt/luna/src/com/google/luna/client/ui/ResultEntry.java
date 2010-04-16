// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.test.TestCase;

public class ResultEntry extends Composite {

  interface IMyUiBinder extends UiBinder<Widget, ResultEntry> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField SpanElement name;
  @UiField SpanElement description;
  private final TestCase test;

  public ResultEntry(TestCase test, String text) {
    this.test = test;
    initWidget(BINDER.createAndBindUi(this));
    this.name.setInnerText(test.getLabel());
    this.description.setInnerText(test.getDescription());
  }

}
