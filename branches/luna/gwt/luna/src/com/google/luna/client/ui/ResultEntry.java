// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.test.ITestCase;

public class ResultEntry extends Composite {

  public interface Resources extends ClientBundle {

    public interface Css extends CssResource {
      String outer();
      String description();
      String name();
    }

    @Source("ResultEntry.css")
    Css css();

  }

  private static final Resources RESOURCES = GWT.create(Resources.class);

  public static Resources getResources() {
    return RESOURCES;
  }

  interface IMyUiBinder extends UiBinder<Widget, ResultEntry> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField(provided=true) final Resources resources = getResources();
  @UiField PromiseLabel name;
  @UiField PromiseLabel description;

  public ResultEntry(ITestCase test, String text) {
    initWidget(BINDER.createAndBindUi(this));
    this.name.setText(test.getLabel());
    this.description.setText(test.getDescription());
  }

}
