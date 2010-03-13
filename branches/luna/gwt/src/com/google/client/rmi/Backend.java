// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client.rmi;

import com.google.client.utils.Promise;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class Backend {
  
  private final RequestTarget cases;
  private final RequestTarget suite;
  private final RequestTarget suites;
  
  public Backend() {
    this.cases = new RequestTarget("data/cases.json");
    this.suite = new RequestTarget("data/suite.json");
    this.suites = new RequestTarget("inspect/suites.json");
  }
  
  public static class Case extends JavaScriptObject {
    protected Case() { }    
    public final native boolean isNegative() /*-{ return !!this.n; }-*/;
  }
  
  public static class CaseBlock extends JavaScriptObject {
    protected CaseBlock() { }
    public final native int getStart() /*-{ return this.s; }-*/;
    public final native int getEnd() /*-{ return this.e; }-*/;
    public final native Case getCase(int index) /*-{ return this.v[index]; }-*/;
  }

  public Promise<CaseBlock> getCases(Key suite, int from, int to) {
    return cases
        .newRequest()
        .addParameter("from", from)
        .addParameter("to", to)
        .addParameter("suite", toString(suite))
        .send();
  }
  
  private static String toString(Key key) {
    return key.getKind() + "." + key.getId();
  }

  public static class Key extends JavaScriptObject {
    protected Key() { }
    public final native String getId() /*-{ return this.i; }-*/;
    public final native String getKind() /*-{ return this.k; }-*/;
    public final String asString() { return getKind() + "." + getId(); }
  }

  public static class Suite extends JavaScriptObject {
    protected Suite() { }
    public final native String getFamily() /*-{ return this.f; }-*/;
    public final native String getName() /*-{ return this.n; }-*/;
    public final native int getSize() /*-{ return this.s; }-*/;
    public final native Key getKey() /*-{ return this.k; }-*/;
  }

  public Promise<Suite> getActiveSuite(final String family, final String name) {
    return suite
        .newRequest()
        .addParameter("family", family)
        .addParameter("name", name)
        .send();
  }
  
  public Promise<JsArray<Suite>> getSuites() {
    return suites
        .newRequest()
        .send();
  }
  
}
