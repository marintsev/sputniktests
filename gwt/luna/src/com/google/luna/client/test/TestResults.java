package com.google.luna.client.test;

import java.util.ArrayList;
import java.util.List;


public class TestResults {

  public interface IListener {
    public void testOver(TestCase test, boolean hadExpectedResult);
  }

  private enum State { RUNNING, UNEXPECTED, EXPECTED }

  private final State[] results;
  private int resultCount = 0;
  private int expectedCount = 0;
  private int unexpectedCount = 0;
  private List<IListener> listeners = new ArrayList<IListener>();

  public TestResults(TestPackage pack) {
    this.results = new State[pack.getTestCount()];
  }

  public void addListener(IListener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(IListener listener) {
    this.listeners.remove(listener);
  }

  public void recordUnexpectedResult(TestCase test) {
    int index = test.getSerial();
    assert results[index] != null;
    results[index] = State.UNEXPECTED;
  }

  public void recordExpectedResult(TestCase test) {
    int index = test.getSerial();
    if (results[index] == State.UNEXPECTED)
      return;
    results[index] = State.EXPECTED;
  }

  public void testStarted(TestCase test) {
    int index = test.getSerial();
    assert results[index] == null;
    results[index] = State.RUNNING;
  }

  public void testOver(TestCase test) {
    int index = test.getSerial();
    assert results[index] != null;
    if (results[index] == State.RUNNING) {
      results[index] = test.isNegative() ? State.EXPECTED : State.UNEXPECTED;
    }
    assert resultCount == index;
    resultCount = index + 1;
    boolean hadExpectedResult = results[index] == State.EXPECTED;
    if (hadExpectedResult) this.expectedCount++;
    else this.unexpectedCount++;
    assert resultCount == this.expectedCount + this.unexpectedCount;
    for (IListener listener : listeners)
      listener.testOver(test, hadExpectedResult);
  }

  public int getExpectedCount() {
    return this.expectedCount;
  }

  public int getUnexpectedCount() {
    return this.unexpectedCount;
  }

}
