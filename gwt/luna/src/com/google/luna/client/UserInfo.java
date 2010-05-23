package com.google.luna.client;

import com.google.gwt.core.client.JavaScriptObject;
/**
 * Information about the current user.
 */
public final class UserInfo extends JavaScriptObject {

  /**
   * Details about the user.
   */
  public static final class User extends JavaScriptObject {

    protected User() { }

    /**
     * Returns the user's nickname.
     */
    public native String getNickname() /*-{ return this.n; }-*/;

  }

  protected UserInfo() { }

  /**
   * Returns the user details.
   */
  public native User getUser() /*-{ return this.u; }-*/;

  /**
   * Is this user allowed to do manager tasks?  Note that this method
   * does not grant any capabilities to the user, it is simply a hint
   * about whether to show the manager parts of the UI.  The server
   * will check whether the user is genuinely a manager before allowing
   * any manager tasks to be executed.
   */
  public native boolean isManager() /*-{ return !!this.m; }-*/;

}
