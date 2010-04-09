// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TabButton extends Composite implements HasText {

	interface IMyUiBinder extends UiBinder<Widget, TabButton> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

	public interface IResources extends ClientBundle {

		public interface ICss extends CssResource {

			String current();
			String text();
			String frame();
			String middle();

		}

		@Source("TabButton.css")
		ICss css();

	}

	public static IResources RESOURCES = GWT.create(IResources.class);

	public static IResources getResources() {
		return RESOURCES;
	}

	public static IResources.ICss getCss() {
		return getResources().css();
	}


  @UiField Label label;

  public TabButton() {
  	initWidget(BINDER.createAndBindUi(this));
  }

  public void setTarget(final String target) {
  	this.addDomHandler(new ClickHandler() {
  		@Override
  		public void onClick(ClickEvent event) {
  			Window.open(target, "_top", null);
  		}
  	}, ClickEvent.getType());
  }

  @Override
  public void setText(String text) {
  	label.setText(text);
  }

  @Override
  public String getText() {
  	return label.getText();
  }

	public void select() {
		getCss().ensureInjected();
		this.addStyleName(getCss().current());
	}

}
