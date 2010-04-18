// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client;

import com.google.gwt.core.client.JavaScriptObject;

public final class UserState extends JavaScriptObject {

	protected UserState() { }

	public static final class WithoutUser extends JavaScriptObject {

		protected WithoutUser() { }

		public native String getLoginUrl() /*-{ return this.i; }-*/;

	}

	public static final class WithUser extends JavaScriptObject {

		protected WithUser() { }

		public native UserInfo getUserInfo() /*-{ return this.u; }-*/;

		public native String getLogoutUrl() /*-{ return this.o; }-*/;

	}

	public native boolean hasUser() /*-{ return 'u' in this; }-*/;

	public native WithoutUser withoutUser() /*-{ return this; }-*/;

	public native WithUser withUser() /*-{ return this; }-*/;

}
