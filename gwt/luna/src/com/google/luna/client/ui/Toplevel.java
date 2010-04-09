// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.Luna;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.utils.Thunk;

public class Toplevel extends Composite {

  interface IMyUiBinder extends UiBinder<Widget, Toplevel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField Label subtitle;
  @UiField TabButton aboutButton;
  @UiField TabButton runButton;
  @UiField TabButton compareButton;
  @UiField(provided=true) final PageView contents;

  public Toplevel(IPageView contents) {
  	this.contents = (PageView) contents;
  	initWidget(BINDER.createAndBindUi(this));
  	setVersion("#");
  }

  public void init() {
  	Luna.getActivePackage().onValue(new Thunk<TestPackage>() {
  		@Override
  		public void onValue(TestPackage pack) {
  			setVersion(pack.getVersion());
  		}
  	});
  }

  private void setVersion(String version) {
  	String subtitleText = Luna.getMessages().subtitle(version);
  	if (Luna.isDevelMode())
  		subtitleText += " (DEVEL)";
  	subtitle.setText(subtitleText);
  }

}
