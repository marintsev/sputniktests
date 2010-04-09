// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class TestRun {

	public interface Listener {
		public void testStarted(TestCase test);
		public void allDone();
	}

	private final TestPackage pack;
	private final Listener listener;
	private int current = 0;
	private boolean isPaused = false;

	public TestRun(TestPackage pack, Listener listener) {
		this.pack = pack;
		this.listener = listener;
	}

	public void start() {
		scheduleNextTest();
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
		if (!isPaused)
			scheduleNextTest();
	}

	private void scheduleNextTest() {
		if (isPaused)
		  return;
		if (current == pack.getTestCount()) {
			listener.allDone();
			return;
		}
		int nextCase = current++;
		pack.getCase(nextCase).onValue(new Thunk<TestCase>() {
			@Override
			public void onValue(TestCase test) {
				runTest(test);
			}
		});
	}

	private void runTest(TestCase test) {
		listener.testStarted(test);
		Promise.defer().onValue(new Thunk<Object>() {
			@Override
			public void onValue(Object t) {
				scheduleNextTest();
			}
		});
	}

}
