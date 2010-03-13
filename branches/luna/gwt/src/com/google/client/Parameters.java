/**
 *
 */
package com.google.client;

import com.google.gwt.core.client.JavaScriptObject;

public final class Parameters extends JavaScriptObject {

	protected Parameters() { }

	public native String getRendererName() /*-{ return this.r; }-*/;

	public native int getVersion() /*-{ return this.v; }-*/;

}
