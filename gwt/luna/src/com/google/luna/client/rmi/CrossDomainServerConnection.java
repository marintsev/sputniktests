// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.rmi;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.luna.client.utils.Promise;

public class CrossDomainServerConnection extends ServerConnection {

	public static IFactory getFactory(final String host) {
		return new IFactory() {
			@Override
			public ServerConnection create() {
				return new CrossDomainServerConnection(host);
			}
		};
	}

	public CrossDomainServerConnection(String host) {
		super(host);
	}

	private static class MessageReceiver<T> {

		private static int nextIndex;

		private final int index;
		private final Promise<T> result;

		public MessageReceiver(Promise<T> result) {
			this.index = nextIndex++;
			this.result = result;
		}

		public int getIndex() {
			return this.index;
		}

		public void inject(String path) {
			doInject(this.index, path);
		}

		@SuppressWarnings("unchecked")
		public void messageReceived(Object obj) {
			result.setValue((T) obj);
		}

		private native void doInject(int index, String path) /*-{
			var script = document.createElement('script');
			var self = this;
			top.pendingMessages = top.pendingMessages || {};
			top.pendingMessages[index] = function (value) {
				delete top.pendingMessages[index];
				document.body.removeChild(script);
				self.@com.google.luna.client.rmi.CrossDomainServerConnection.MessageReceiver::messageReceived(Ljava/lang/Object;)(value);
			};
			script.src = path;
			document.body.appendChild(script);
	  }-*/;

	}

	@Override
	public <T extends JavaScriptObject> Promise<T> send(Message message) {
		Promise<T> result = new Promise<T>();
		MessageReceiver<T> receiver = new MessageReceiver<T>(result);
		String callback = "top.pendingMessages[" + receiver.getIndex() + "]";
		message.addParameter("callback", callback);
		String path = message.getAbsolutePath();
		receiver.inject(path);
		return result;
	}

}
