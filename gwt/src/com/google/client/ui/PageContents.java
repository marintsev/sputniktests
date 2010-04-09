package com.google.client.ui;

import com.google.client.rmi.Backend;
import com.google.gwt.user.client.ui.Composite;

public abstract class PageContents extends Composite {

	public interface Factory {
		public PageContents create();
	}

	private Backend backend;

	public void initialize(Backend backend) {
		this.backend = backend;
	}

	protected Backend getBackend() {
		return this.backend;
	}

}
