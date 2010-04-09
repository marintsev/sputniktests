package com.google.client.ui;

import com.google.client.Sputnik;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Page extends Composite {

	interface PageUiBinder extends UiBinder<Widget, Page> {}
  private static PageUiBinder BINDER = GWT.create(PageUiBinder.class);

  private static SputnikMessages MESSAGES = Sputnik.getMessages();

  @UiField Label title;
  @UiField Label subtitle;

  @UiField TabButton aboutButton;
  @UiField TabButton runButton;
  @UiField TabButton compareButton;

  @UiField(provided=true) final PageContents contents;

  public Page(PageContents contents) {
  	this.contents = contents;
  	initWidget(BINDER.createAndBindUi(this));
  	title.setText(MESSAGES.title());
  	subtitle.setText(MESSAGES.subtitle(Sputnik.getParameters().getVersion()));
  	aboutButton.setText(MESSAGES.aboutLabel());
  	runButton.setText(MESSAGES.runLabel());
  	compareButton.setText(MESSAGES.compareLabel());
  }

}
