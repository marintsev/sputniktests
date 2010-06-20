package com.google.luna.client.test;

import com.google.luna.client.test.PersistentTestResults.TestState;
import com.google.luna.client.test.TestOutcome.Status;
import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.test.data.ITestPackage;
import com.google.luna.client.utils.Cookie;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;
/**
 * Utility class that produces test cases, monitors their outcome, and
 * persists the results in cookies.
 */
public class TestProgress {

  private final ITestPackage pack;
  private final PersistentTestResults results;
  private final EventTranslater eventTranslater;
  private int nextTest;

  private int expectedOutcomeCount = 0;
  private int unexpectedOutcomeCount = 0;

  public class TestMonitor {

    private final ITestCase test;
    private final ITestEventHandler handler;

    public TestMonitor(ITestCase test) {
      this.test = test;
      this.handler = eventTranslater.getEventListener(test);
    }

    public ITestCase getCase() {
      return this.test;
    }

    public ITestEventHandler getHandler() {
      return this.handler;
    }

  }

  public TestProgress(ITestPackage pack, Cookie.Factory cookieFactory) {
    this.pack = pack;
    this.results = new PersistentTestResults(pack, cookieFactory.child("results"));
    this.nextTest = results.getResultCount();
    this.eventTranslater = new EventTranslater();
    this.eventTranslater.addListener(new EventTranslater.ITestResultListener() {
      @Override
      public void onTestDone(TestOutcome outcome) {
        registerResult(outcome);
      }
      @Override
      public void onTestStarting(ITestCase test) {
        // ignore
      }
    });
    fastForwardStats();
  }

  public void addListener(EventTranslater.ITestResultListener listener) {
    this.eventTranslater.addListener(listener);
  }

  public void removeListener(EventTranslater.ITestResultListener listener) {
    this.eventTranslater.removeListener(listener);
  }

  public boolean hasNext() {
    return nextTest < pack.getTestCount();
  }

  public Promise<ITestCase> peekNextCase() {
    assert hasNext();
    return pack.getCase(nextTest);
  }

  public Promise<TestMonitor> getNext() {
    assert hasNext();
    final Promise<TestMonitor> pResult = new Promise<TestMonitor>();
    int serial = nextTest++;
    pack.getCase(serial).onValue(new Thunk<ITestCase>() {
      @Override
      public void onValue(ITestCase test) {
        pResult.setValue(new TestMonitor(test));
      }
    });
    return pResult;
  }

  private void fastForwardStats() {
    int count = results.getResultCount();
    for (int i = 0; i < count; i++) {
      if (results.getState(i) == TestState.PASSED) {
        expectedOutcomeCount++;
      } else {
        unexpectedOutcomeCount++;
      }
    }
  }

  protected void registerResult(TestOutcome outcome) {
    if (outcome.getStatus() == Status.EXPECTED) {
      results.setState(outcome.getSerial(), TestState.PASSED);
      expectedOutcomeCount++;
    } else {
      results.setState(outcome.getSerial(), TestState.FAILED);
      unexpectedOutcomeCount++;
    }
  }

  public int getExpectedOutcomeCount() {
    return expectedOutcomeCount;
  }

  public int getUnexpectedOutcomeCount() {
    return unexpectedOutcomeCount;
  }

  public int getTestCompleteCount() {
    return expectedOutcomeCount + unexpectedOutcomeCount;
  }

  public boolean hasMoreTests() {
    return nextTest < pack.getTestCount();
  }

}
