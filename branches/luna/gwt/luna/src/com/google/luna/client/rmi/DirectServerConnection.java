// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.rmi;

import java.io.IOException;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.luna.client.utils.Promise;

public class DirectServerConnection extends ServerConnection {

	public static IFactory getFactory(final String root) {
		return new IFactory() {
			@Override
			public ServerConnection create() {
				return new DirectServerConnection(root);
			}
		};
	}

	public DirectServerConnection(String url) {
		super(url);
	}

	public <T extends JavaScriptObject> Promise<T> send(Message message) {
		String path = message.getAbsolutePath();
  	RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, path);
  	final Promise<T> result = new Promise<T>();
  	try {
  		builder.sendRequest(null, new RequestCallback() {
  			public void onError(Request request, Throwable exception) {
  				result.setError(exception);
  			}
  			public void onResponseReceived(Request request, Response response) {
  				if (200 == response.getStatusCode()) {
  					String text = response.getText();
  					result.setValue(JsonUtils.<T>unsafeEval(text));
  				} else {
  					result.setError(new IOException("Request error: " + response.getStatusCode()));
  				}
  			}
  		});
  	} catch (RequestException re) {
  		result.setError(re);
  	}
  	return result;
  }

}
