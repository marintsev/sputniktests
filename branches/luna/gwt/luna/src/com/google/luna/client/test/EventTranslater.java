package com.google.luna.client.test;

import com.google.luna.client.test.TestOutcome.Status;
import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.utils.Listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utility class that dispenses a sequence of test event listeners
 * and uses the events fired to those listeners to determine which tests
 * have failed and passed.
 */
public class EventTranslater {

  /**
   * Listener for test results.
   */
  public interface ITestResultListener {

    public void onTestStarting(ITestCase test);

    /**
     * A test has been run and the result is given as an argument.
     */
    public void onTestDone(TestOutcome outcome);

  }

  private final Listeners<ITestResultListener> listeners = new Listeners<ITestResultListener>();
  private EventListener active = null;

  public ITestEventHandler getEventListener(ITestCase test) {
    if (active != null) {
      assert active.getTest().getSerial() < test.getSerial();
      active.onDone();
    }
    active = new EventListener(test);
    return active;
  }

  public void addListener(ITestResultListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ITestResultListener listener) {
    listeners.remove(listener);
  }

  private void fireTestDone(TestOutcome outcome) {
    for (ITestResultListener listener : listeners)
      listener.onTestDone(outcome);
  }

  private void fireTestStarting(ITestCase test) {
    for (ITestResultListener listener : listeners)
      listener.onTestStarting(test);
  }

  private class EventListener implements ITestEventHandler {

    private final ITestCase test;
    private String error = null;
    private List<String> messages = Collections.emptyList();
    private boolean isComplete = false;

    public EventListener(ITestCase test) {
      this.test = test;
    }

    public ITestCase getTest() {
      return this.test;
    }

    @Override
    public void onError(String message) {
      error = message == null ? "" : message;
    }

    @Override
    public void onMessage(String message) {
      if (messages.isEmpty())
        messages = new ArrayList<String>();
      messages.add(message);
    }

    @Override
    public void onAboutToStart() {

    }

    @Override
    public void onComplete() {
      isComplete = true;
    }

    @Override
    public void onLoaded() {
      fireTestStarting(test);
    }

    @Override
    public void onDone() {
      assert active == this;
      active = null;
      boolean didFail = (error != null) || !isComplete;
      boolean shouldFail = test.isNegative();
      Status outcome = (didFail == shouldFail) ? Status.EXPECTED : Status.UNEXPECTED;
      fireTestDone(new TestOutcome(test, outcome, error, messages));
    }

  }

}
