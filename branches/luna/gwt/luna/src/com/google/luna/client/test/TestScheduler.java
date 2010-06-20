// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;


import com.google.luna.client.test.EventTranslater.ITestResultListener;
import com.google.luna.client.test.TestProgress.TestMonitor;
import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.utils.Listeners;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class TestScheduler implements ITestResultListener {

  public interface IListener {
    void allDone();
    void testStarting(ITestCase test);
    void testDone(TestOutcome outcome);
  }

  private final Listeners<IListener> listeners = new Listeners<IListener>();
  private boolean isPaused = false;
  private final TestProgress progress;

  public TestScheduler(TestProgress progress) {
    this.progress = progress;
    progress.addListener(this);
  }

  public TestProgress getProgress() {
    return progress;
  }

  public void addListener(IListener listener) {
    listeners.add(listener);
  }

  public void removeListener(IListener listener) {
    listeners.remove(listener);
  }

  public void start() {
    scheduleNextTest();
  }

  public void setPaused(boolean isPaused) {
    this.isPaused = isPaused;
    if (!isPaused)
      scheduleNextTest();
  }

  private void fireAllDone() {
    for (IListener listener : listeners)
      listener.allDone();
  }


  protected void fireTestStarting(ITestCase test) {
    for (IListener listener : listeners)
      listener.testStarting(test);
  }

  protected void fireTestDone(TestOutcome outcome) {
    for (IListener listener : listeners)
      listener.testDone(outcome);
  }

  private void scheduleNextTest() {
    if (isPaused)
      return;
    if (!progress.hasMoreTests()) {
      fireAllDone();
      return;
    }
    Promise<TestMonitor> next = progress.getNext();
    next.onValue(new Thunk<TestMonitor>() {
      @Override
      public void onValue(TestMonitor monitor) {
        monitor.getCase().schedule(monitor.getHandler());
      }
    });
  }

  @Override
  public void onTestDone(TestOutcome outcome) {
    fireTestDone(outcome);
    deferredScheduleNextTest();
  }

  @Override
  public void onTestStarting(ITestCase test) {
    fireTestStarting(test);
  }

  private void deferredScheduleNextTest() {
    Promise.defer(10).lazyOnValue(new Thunk<Object>() {
      @Override
      public void onValue(Object t) {
        scheduleNextTest();
      }
    });
  }

}
