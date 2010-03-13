package com.google.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Bundle extends ClientBundle {

	public static final Bundle INSTANCE = GWT.create(Bundle.class);

	@Source("about.html")
	TextResource about();

}
