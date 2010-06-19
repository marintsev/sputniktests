package com.google.luna.client.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.utils.Listeners;

/**
 * A utility class that dispenses a sequence of test event listeners
 * and uses the events fired to those listeners to determine which tests
 * have failed and passed.
 */
public class TestEventTranslater {

  /**
   * Listener for test results.
   */
  public interface ITestResultListener {

    public enum TestOutcome { PASS, FAIL };

    /**
     * The given test has been run and the result, pass or fail, is
     * given as an argument.
     */
    public void onTestDone(ITestCase test, TestOutcome outcome,
        List<String> errors, List<String> messages);

  }

  private final Listeners<ITestResultListener> listeners = new Listeners<ITestResultListener>();
  private EventListener active = null;

  public ITestEventListener getEventListener(ITestCase test) {
    if (active != null) {
      assert active.getTest().getSerial() < test.getSerial();
      active.testDone();
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

  private class EventListener implements ITestEventListener {

    private final ITestCase test;
    private List<String> errors = Collections.emptyList();
    private List<String> messages = Collections.emptyList();

    public EventListener(ITestCase test) {
      this.test = test;
    }

    public ITestCase getTest() {
      return this.test;
    }

    @Override
    public void error(String message) {
      if (errors.isEmpty())
        errors = new ArrayList<String>();
      errors.add(message);
    }

    @Override
    public void message(String message) {
      if (messages.isEmpty())
        messages = new ArrayList<String>();
      messages.add(message);
    }

    @Override
    public void testAboutToStart() {
      // TODO Auto-generated method stub

    }

    @Override
    public void testDone() {
      // TODO Auto-generated method stub

    }

    @Override
    public void testLoaded() {
      // TODO Auto-generated method stub

    }

    @Override
    public void testRunToCompletion() {
      // TODO Auto-generated method stub

    }

  }

}
