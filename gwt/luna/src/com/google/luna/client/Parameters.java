// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The parameters and settings set for this instance of sputnik.  This
 * includes selecting which page to show and which user is visiting
 * the page.
 */
public final class Parameters extends JavaScriptObject {

  protected Parameters() { }

  public native String getPageName() /*-{ return this.p; }-*/;

  public native String getDataPath() /*-{ return this.s; }-*/;

  public native boolean isServerSideDevel() /*-{ return !!this.d; }-*/;

  public native boolean isClientSideDevel() /*-{ return !!this.c; }-*/;

  public native boolean isMobile() /*-{ return !!this.m; }-*/;

  public native UserState getUserState() /*-{ return this.u; }-*/;

}
