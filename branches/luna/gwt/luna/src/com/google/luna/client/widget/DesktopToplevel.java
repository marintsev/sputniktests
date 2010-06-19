package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.Luna;
import com.google.luna.client.UserInfo;
import com.google.luna.client.UserState;
import com.google.luna.client.logic.ILogView;
import com.google.luna.client.logic.ToplevelPresenter;

public class DesktopToplevel extends ToplevelWidget {

  @UiTemplate("DesktopToplevel.ui.xml")
  interface IMyUiBinder extends UiBinder<Widget, DesktopToplevel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);
  
  @UiField SpanElement whenUser;
  @UiField SpanElement whenNoUser;
  @UiField SpanElement userName;
  @UiField AnchorElement login;
  @UiField AnchorElement logout;
  @UiField SpanElement manage;
  @UiField SpanElement devModeUi;
  @UiField Anchor toggleLog;
  @UiField LogWidget log;

  protected DesktopToplevel(ToplevelPresenter presenter, IPageView contents) {
    super(presenter, contents);
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
    if (Luna.showDevModeUi()) {
      toggleLog.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          getPresenter().onLogClicked();
        }
      });
    } else {
      devModeUi.getStyle().setVisibility(Visibility.HIDDEN);
    }
  }
  
  @Override
  public void setLogVisible(boolean visible) {
    log.setVisible(visible);
  }
  
  @Override
  public ILogView getLog() {
    return log;
  }

}
