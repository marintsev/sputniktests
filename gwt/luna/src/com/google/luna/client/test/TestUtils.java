// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.ScriptElement;

public class TestUtils {

  public static void injectScript(IFrameElement frame, String source) {
    Document document = frame.getContentDocument();
    ScriptElement script = document.createScriptElement(source);
    document.getBody().appendChild(script);
  }

}
