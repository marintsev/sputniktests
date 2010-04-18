package com.google.luna.client;

import com.google.gwt.core.client.JavaScriptObject;

public final class UserInfo extends JavaScriptObject {

	public static final class User extends JavaScriptObject {

		protected User() { }

		public native String getNickname() /*-{ return this.n; }-*/;

	}

	protected UserInfo() { }

	public native User getUser() /*-{ return this.u; }-*/;

	public native boolean isManager() /*-{ return !!this.m; }-*/;

}
