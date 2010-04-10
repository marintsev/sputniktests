// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.dom.client.Element;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class TestRun implements ITestRun {

	public interface IListener {
		public void testStarted(TestCase test);
		public void allDone();
	}

	private final TestPackage pack;
	private final IListener listener;
	private final Element workspace;
	private int current = 0;
	private boolean isPaused = false;
	private final TestResults results;

	public TestRun(TestPackage pack, IListener listener, Element workspace) {
		this.pack = pack;
		this.listener = listener;
		this.workspace = workspace;
		this.results = new TestResults(pack);
	}

	public TestResults getResults() {
		return this.results;
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
				test.schedule(TestRun.this);
			}
		});
	}

	private void deferredScheduleNextTest() {
		Promise.defer().onValue(new Thunk<Object>() {
			@Override
			public void onValue(Object t) {
				scheduleNextTest();
			}
		});
	}

	public Element getWorkspace() {
		return this.workspace;
	}

	@Override
	public void testStarted(TestCase test) {
		listener.testStarted(test);
	}

	@Override
	public void testScriptComplete(TestCase test) {
		results.setResult(test.getSerial(), test.isNegative()
				? TestResults.Outcome.UNEXPECTED
			  : TestResults.Outcome.EXPECTED);
	}

	@Override
	public void testFailed(TestCase test, String message) {
		results.setResult(test.getSerial(), test.isNegative()
		    ? TestResults.Outcome.EXPECTED
		    : TestResults.Outcome.UNEXPECTED);
	}

	@Override
	public void testPrint(TestCase test, String message) {
		// ignore for now
	}

	@Override
	public void testDone(TestCase test) {
		if (!test.isNegative())
			results.setResult(test.getSerial(), TestResults.Outcome.EXPECTED);
		deferredScheduleNextTest();
	}

}
