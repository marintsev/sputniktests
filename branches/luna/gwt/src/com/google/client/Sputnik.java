// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import com.google.client.rmi.Backend;
import com.google.client.utils.Thunk;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;

import java.util.HashMap;
import java.util.Map;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sputnik implements EntryPoint {
  
  private final Map<String, MainPageRenderer> RENDERERS = new HashMap<String, MainPageRenderer>() {{
    put("Index", new IndexRenderer());
    put("Inspect", new InspectRenderer());
  }};

  public abstract class SimpleThunk<T> implements Thunk<T> {
    public void onError(String message) {
      fatalError(message);
    }
  }
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    String rendererName = getParameters().getRendererName();
    MainPageRenderer renderer = RENDERERS.get(rendererName);
    renderer.initialize(new Backend(), this);
    renderer.render();
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
  
  private static final class AppParameters extends JavaScriptObject {
    protected AppParameters() { }
    public native String getRendererName() /*-{ return this.r; }-*/;
  }
  
  private static native AppParameters getParameters() /*-{
    return top.kParameters;
  }-*/;

}
