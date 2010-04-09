package com.google.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class RunPage extends PageContents {

	public static Factory getFactory() {
		return new Factory() {
			@Override
			public PageContents create() {
				return new RunPage();
			}
		};
	}


	interface RunPageUiBinder extends UiBinder<Widget, RunPage> {}
  private static RunPageUiBinder BINDER = GWT.create(RunPageUiBinder.class);

  public RunPage() {
  	initWidget(BINDER.createAndBindUi(this));
  }

}
