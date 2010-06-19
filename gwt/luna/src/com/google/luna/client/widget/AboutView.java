package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class AboutView extends PageView implements IAboutView {

  interface IMyUiBinder extends UiBinder<Widget, AboutView> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  @UiField SpanElement titleVersion;
  @UiField SpanElement textVersion;
  @UiField SpanElement changelogVersion;

  public AboutView() {
    this.initWidget(BINDER.createAndBindUi(this));
  }

  @Override
  public void setVersion(String version) {
    titleVersion.setInnerText(version);
    textVersion.setInnerText(version);
    changelogVersion.setInnerText(version);
  }

}
