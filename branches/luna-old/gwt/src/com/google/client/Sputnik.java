// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import java.util.HashMap;
import java.util.Map;

import com.google.client.rmi.Backend;
import com.google.client.ui.AboutPage;
import com.google.client.ui.Page;
import com.google.client.ui.PageContents;
import com.google.client.ui.RunPage;
import com.google.client.ui.SputnikMessages;
import com.google.client.utils.Thunk;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sputnik implements EntryPoint {

  private final Map<String, PageContents.Factory> FACTORIES = new HashMap<String, PageContents.Factory>() {{
    put("Index", AboutPage.getFactory());
    put("Run", RunPage.getFactory());
  }};

  private static SputnikMessages MESSAGES = GWT.create(SputnikMessages.class);

  public abstract class SimpleThunk<T> implements Thunk<T> {
    public void onError(String message) {
      fatalError(message);
    }
  }

  public static SputnikMessages getMessages() {
  	return MESSAGES;
  }

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    String rendererName = getParameters().getRendererName();
    PageContents.Factory factory = FACTORIES.get(rendererName);
    PageContents contents = factory.create();
    contents.initialize(new Backend());
    Page page = new Page(contents);
    RootPanel.get().add(page);
  }

  public void fatalError(String message) {
    DialogBox dialog = new DialogBox();
    dialog.setGlassEnabled(true);
    dialog.setAnimationEnabled(true);
    dialog.setText("Fatal Error");
    Label label = new Label(message);
    label.setWidth("300px");
    dialog.setWidget(label);
    dialog.center();
    dialog.show();
  }

  public void fatalError(Exception e) {
    fatalError(e.getMessage());
  }

  public static native Parameters getParameters() /*-{
    return top.kParameters;
  }-*/;

}
