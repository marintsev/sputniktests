// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.rmi;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.luna.client.utils.Promise;
/**
 * Bridge between the higher-level code and the connection to the server.
 * Transforms getter calls into requests through the connection and
 * parses the result into data objects.
 */
public class Backend {

  private final ServerConnection server;

  public Backend(ServerConnection.IFactory factory) {
    this.server = factory.create();
  }

  /**
   * A database reference.
   */
  public static class Key extends JavaScriptObject {
    protected Key() { }
    public final native String getId() /*-{ return this.i; }-*/;
    public final native String getKind() /*-{ return this.k; }-*/;
    public final String asString() { return getKind() + "." + getId(); }
  }

  /**
   * Test suite metadata.  Use the key to fetch individual tests.
   */
  public static class Suite extends JavaScriptObject {
    protected Suite() { }
    public final native String getType() /*-{ return this.t; }-*/;
    public final native int getCaseCount() /*-{ return this.c; }-*/;
    public final native Key getKey() /*-{ return this.k; }-*/;
  }

  /**
   * Test package metadata.
   */
  public static class Package extends JavaScriptObject {
    protected Package() { }
    public final native JsArray<Suite> getSuites() /*-{ return this.s; }-*/;
    public final native String getVersion() /*-{ return this.v; }-*/;
  }

  /**
   * Makes a request for the currently active package.
   */
  public Promise<Package> getActivePackage() {
    return server
        .newRequest("activePackage.json")
        .send();
  }

  /**
   * Full data of a single test case.
   */
  public static class Case extends JavaScriptObject {
    protected Case() { }
    public final native String getName() /*-{ return this.n; }-*/;
    public final native String getSection() /*-{ return this.s; }-*/;
    public final native String getSource() /*-{ return this.c; }-*/;
  }

  /**
   * A block of test cases containing the actual test cases.
   */
  public static class CaseBlock extends JavaScriptObject {
    protected CaseBlock() { }
    public final native int getStart() /*-{ return this.s; }-*/;
    public final native int getEnd() /*-{ return this.e; }-*/;
    public final native JsArray<Case> getCases() /*-{ return this.c; }-*/;
  }

  /**
   * Makes a request for the specified range of tests from the specified
   * suite.
   */
  public Promise<CaseBlock> getCases(Suite suite, int from, int to) {
    return server
        .newRequest("cases.json")
        .addParameter("suite", suite.getKey().asString())
        .addParameter("from", from)
        .addParameter("to", to)
        .send();
  }

}
