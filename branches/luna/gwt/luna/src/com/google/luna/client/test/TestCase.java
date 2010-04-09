// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.luna.client.rmi.Backend;

public class TestCase {

	private final Backend.Case data;
	private final int serial;
	private final ITestController controller;

	public TestCase(Backend.Case data, int serial, ITestController controller) {
		this.data = data;
		this.serial = serial;
		this.controller = controller;
	}

	public int getSerial() {
		return this.serial;
	}

	public String getName() {
		return data.getName();
	}

	public String getSection() {
		return data.getSection();
	}

	@Override
	public String toString() {
		return "test case { id: #" + this.serial + ", name: " + data.getName() +
		    ", section: " + data.getSection() + " }";
	}

	public void run() {
		controller.run(this);
	}

}
