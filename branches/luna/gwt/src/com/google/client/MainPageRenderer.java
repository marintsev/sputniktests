// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import com.google.client.rmi.Backend;
import com.google.client.ui.SputnikPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class MainPageRenderer {

  private Backend backend;
  private Sputnik sputnik;

  public void initialize(Backend backend, Sputnik sputnik) {
    this.backend = backend;
    this.sputnik = sputnik;
  }

  protected Backend getBackend() {
    return this.backend;
  }

  protected Sputnik getSputnik() {
    return this.sputnik;
  }

  protected Parameters getParameters() {
  	return this.sputnik.getParameters();
  }

  public void renderPage(Panel root) {
  	root.setStylePrimaryName("root");

  	Panel main = new VerticalPanel();
  	main.setStylePrimaryName("main");

  	// Header
  	Panel header = new VerticalPanel();
  	header.setStylePrimaryName("header");
  	main.add(header);
  	Label title = new Label("Sputnik");
  	title.setStylePrimaryName("title");
  	header.add(title);
  	int version = getParameters().getVersion();
  	Label subtitle = new Label("JavaScript Conformance - version " + version);
  	subtitle.setStylePrimaryName("subtitle");
  	header.add(subtitle);
    root.add(main);

    // Navigation
    Panel navigation = new HorizontalPanel();
    navigation.setStylePrimaryName("navigation");
    main.add(navigation);

    Panel about = new SputnikPanel();
    about.add(new Label("About"));
    navigation.add(about);

    Panel run = new SputnikPanel();
    run.add(new Label("Run"));
    navigation.add(run);

    Panel compare = new SputnikPanel();
    compare.add(new Label("Compare"));
    navigation.add(compare);

    // Contents
    Panel contents = new SputnikPanel();
    main.add(contents);
    renderContents(contents);
  }

  public abstract void renderContents(Panel root);

}
