package com.google.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;


public class AboutPage extends PageContents {

	public static Factory getFactory() {
		return new Factory() {
			@Override
			public PageContents create() {
				return new AboutPage();
			}
		};
	}

	interface AboutPageUiBinder extends UiBinder<Widget, AboutPage> {}
  private static AboutPageUiBinder BINDER = GWT.create(AboutPageUiBinder.class);

  public AboutPage() {
  	initWidget(BINDER.createAndBindUi(this));
  }

}
