package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.Luna;
import com.google.luna.client.UserInfo;
import com.google.luna.client.UserState;

public class DesktopToplevel extends Toplevel {

	@UiTemplate("DesktopToplevel.ui.xml")
  interface IMyUiBinder extends UiBinder<Widget, DesktopToplevel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField SpanElement whenUser;
  @UiField SpanElement whenNoUser;
  @UiField SpanElement userName;
  @UiField AnchorElement login;
  @UiField AnchorElement logout;
  @UiField SpanElement manage;

	protected DesktopToplevel(IPageView contents) {
		super(contents);
		initWidget(BINDER.createAndBindUi(this));
	}

	@Override
	public void initUi() {
		super.initUi();
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
	}

}
