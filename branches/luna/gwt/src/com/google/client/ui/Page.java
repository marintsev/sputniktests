package com.google.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Page extends Composite {

	interface MyUiBinder extends UiBinder<Widget, Page> {}
  private static MyUiBinder BINDER = GWT.create(MyUiBinder.class);

  public Page() {
  	initWidget(BINDER.createAndBindUi(this));
  }

}
