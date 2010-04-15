// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ResultList extends Composite {

  interface IMyUiBinder extends UiBinder<Widget, ResultList> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  public ResultList() {
    initWidget(BINDER.createAndBindUi(this));
  }

}
