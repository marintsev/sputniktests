// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client.rmi;

import com.google.client.utils.Promise;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

public class RequestTarget {

  public class Message {

    private String path = url;
    private boolean first = true;

    public Message addParameter(String name, Object value) {
      String prefix;
      if (first) {
        prefix = "?";
        first = false;
      } else {
        prefix = "&";
      }
      path += prefix + URL.encode(name) + "=" + URL.encode(value.toString());
      return this;
    }

    public <T extends JavaScriptObject> Promise<T> send() {
      RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, path);
      final Promise<T> result = new Promise<T>();
      try {
        builder.sendRequest(null, new RequestCallback() {
          public void onError(Request request, Throwable exception) {
            result.setError(exception.toString());
          }
          public void onResponseReceived(Request request, Response response) {
            if (200 == response.getStatusCode()) {
              String text = response.getText();
              result.setValue(JsonUtils.<T>unsafeEval(text));
            } else {
              result.setError("Error: " + response.getStatusCode());
            }
          }
        });
      } catch (RequestException re) {
        result.setError(re.getMessage());
      }
      return result;
    }

  }

  private final String url;

  public RequestTarget(String url) {
    this.url = URL.encode("../../" + url);
  }

  public Message newRequest() {
    return new Message();
  }

}
