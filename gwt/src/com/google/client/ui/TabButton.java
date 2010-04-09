package com.google.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TabButton extends Composite {

	interface TabButtonUiBinder extends UiBinder<Widget, TabButton> {}
  private static TabButtonUiBinder BINDER = GWT.create(TabButtonUiBinder.class);

  @UiField Label label;

  public TabButton() {
  	initWidget(BINDER.createAndBindUi(this));
  }

  public void setText(String text) {
  	label.setText(text);
  }

}
