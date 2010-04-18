// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.Luna;
import com.google.luna.client.UserInfo;
import com.google.luna.client.UserState;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.utils.Thunk;

public class Toplevel extends Composite {

  interface IMyUiBinder extends UiBinder<Widget, Toplevel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField Label subtitle;
  @UiField TabButton aboutButton;
  @UiField TabButton runButton;
  @UiField TabButton compareButton;
  @UiField SpanElement whenUser;
  @UiField SpanElement whenNoUser;
  @UiField SpanElement userName;
  @UiField AnchorElement login;
  @UiField AnchorElement logout;
  @UiField SpanElement manage;
  @UiField(provided=true) final PageView contents;

  public Toplevel(IPageView contents) {
  	this.contents = (PageView) contents;
  	initWidget(BINDER.createAndBindUi(this));
  	UserState state = Luna.getParameters().getUserState();
  	boolean showManage = false;
  	if (state.hasUser()) {
  		UserInfo userInfo = state.withUser().getUserInfo();
  		whenNoUser.getStyle().setDisplay(Display.NONE);
  		userName.setInnerText(userInfo.getUser().getNickname());
  		logout.setHref(state.withUser().getLogoutUrl());
  		showManage = userInfo.isManager();
  	} else {
  		whenUser.getStyle().setDisplay(Display.NONE);
  		login.setHref(state.withoutUser().getLoginUrl());
  	}
  	if (!showManage)
  		manage.getStyle().setDisplay(Display.NONE);
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
