package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;

public class MobileToplevel extends Toplevel {

	@UiTemplate("MobileToplevel.ui.xml")
  interface IMyUiBinder extends UiBinder<Widget, MobileToplevel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

	protected MobileToplevel(IPageView contents) {
		super(contents);
		initWidget(BINDER.createAndBindUi(this));
	}

}
