// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class TestRun implements ITestProgressSink {

  public interface IListener extends TestResults.IListener {
    public void testStarted(ITestCase test);
    public void allDone();
  }

  private final TestPackage pack;
  private final IListener listener;
  private int current = 0;
  private boolean isPaused = false;
  private final TestResults results;

  public TestRun(TestPackage pack, IListener listener) {
    this.pack = pack;
    this.listener = listener;
    this.results = new TestResults(pack);
    this.results.addListener(listener);
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
    pack.getCase(nextCase).onValue(new Thunk<ITestCase>() {
      @Override
      public void onValue(ITestCase test) {
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

  @Override
  public void testStarted(ITestCase test) {
    results.testStarted(test);
    listener.testStarted(test);
  }

  @Override
  public void testScriptComplete(ITestCase test) {
    if (test.isNegative()) {
      results.recordUnexpectedResult(test);
    } else {
      results.recordExpectedResult(test);
    }
  }

  @Override
  public void testFailed(ITestCase test, String message) {
    if (test.isNegative()) {
      results.recordExpectedResult(test);
    } else {
      results.recordUnexpectedResult(test);
    }
  }

  @Override
  public void testPrint(ITestCase test, String message) {
    // ignore for now
  }

  @Override
  public void testDone(ITestCase test) {
    results.testOver(test);
    deferredScheduleNextTest();
  }

  @Override
  public void testSkipped(ITestCase test) {
    results.recordExpectedResult(test);
    results.testOver(test);
    deferredScheduleNextTest();
  }

}
