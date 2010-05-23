// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * State of the current visitor.  The visitor may either be logged in,
 * in which case this gives access to user information, or not logged in,
 * in which case information is available about how they could log in.
 * Use {@link #hasUser()} to determine which is the case.
 */
public final class UserState extends JavaScriptObject {

  protected UserState() { }

  /**
   * View on the visitor data that is valid when the visitor is not logged
   * in.
   */
  public static final class WithoutUser extends JavaScriptObject {

    protected WithoutUser() { }

    /**
     * The URL to visit to log in.
     */
    public native String getLoginUrl() /*-{ return this.i; }-*/;

  }

  /**
   * View on the visitor data that is valid when the visitor is logged in.
   *
   */
  public static final class WithUser extends JavaScriptObject {

    protected WithUser() { }

    /**
     * Returns the user's details.
     */
    public native UserInfo getUserInfo() /*-{ return this.u; }-*/;

    /**
     * Returns the URL to use to log out.
     */
    public native String getLogoutUrl() /*-{ return this.o; }-*/;

  }

  /**
   * Is the visitor logged in?
   */
  public native boolean hasUser() /*-{ return 'u' in this; }-*/;

  /**
   * Returns the view that is valid when the visitor is not logged in.
   */
  public native WithoutUser withoutUser() /*-{ return this; }-*/;

  /**
   * Returns the view that is valid when the visitor is logged in.
   */
  public native WithUser withUser() /*-{ return this; }-*/;

}
