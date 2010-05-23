// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.rmi;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import com.google.luna.client.Luna;
import com.google.luna.client.utils.Promise;

/**
 * A raw connection to the test data server.  Subclasses define how
 * requests are actually sent.  Luna may come with some requests
 * pre-populated in which case the connection just returns the canned
 * answer rather than actually make the request.
 */
public abstract class ServerConnection {

  /**
   * Factory interface; creates a new connection.
   */
  public interface IFactory {
    public ServerConnection create();
  }

  /**
   * Root URL.
   */
  private final String root;

  public ServerConnection(String root) {
    this.root = URL.encode(root);
  }

  public Message newRequest(String path) {
    return new Message(path);
  }

  /**
   * Sends the given message over the wire.
   */
  protected abstract <T extends JavaScriptObject> Promise<T> send(Message message);

  public class Message {

    private final String path;
    private final ArrayList<String[]> params;
    private String relativePath;

    public Message(String path) {
      this.path = path;
      this.params = new ArrayList<String[]>();
    }

    public Message addParameter(String name, Object value) {
      String uName = URL.encode(name);
      String uValue = URL.encode(value.toString());
      params.add(new String[] {uName, uValue});
      relativePath = null;
      return this;
    }

    public String getRelativePath() {
      if (relativePath == null)
        relativePath = buildRelativePath();
      return this.relativePath;
    }

    private String buildRelativePath() {
      StringBuilder buf = new StringBuilder(this.path);
      boolean first = true;
      for (String[] param : this.params) {
        if (first) {
          first = false;
          buf.append("?");
        } else {
          buf.append("&");
        }
        buf.append(param[0]).append('=').append(param[1]);
      }
      return buf.toString();
    }

    public String getAbsolutePath() {
      return root + this.getRelativePath();
    }

    @SuppressWarnings("unchecked")
    public <T extends JavaScriptObject> Promise<T> send() {
      String path = getRelativePath();
      Object canned = Luna.getCannedRequest(path);
      if (canned != null) {
        return Promise.from((T) canned);
      } else {
        return ServerConnection.this.send(this);
      }
    }

  }

}
