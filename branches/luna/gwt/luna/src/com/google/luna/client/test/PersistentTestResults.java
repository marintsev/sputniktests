package com.google.luna.client.test;

import com.google.luna.client.test.data.ITestPackage;
import com.google.luna.client.utils.Cookie;
import com.google.luna.client.utils.EnumVector;
import com.google.luna.client.utils.PersistentEnumVector;

/**
 * A compact representation of the results of a test run.  This is the
 * raw pass/fail/crash data without a log of errors, etc., which is
 * stored in cookies.
 */
public class PersistentTestResults {

  public enum TestState {
    PASSED, FAILED, CRASHED
  }

  private static final int kVectorSegmentSize = 256;
  private final Cookie<Integer> resultCount;
  private final EnumVector<TestState> states;

  public PersistentTestResults(ITestPackage tests, Cookie.Factory cookieFactory) {
    this.states = new PersistentEnumVector<TestState>(TestState.values(),
        tests.getTestCount(), kVectorSegmentSize, cookieFactory.child("state"));
    this.resultCount = cookieFactory.integer("resultCount");
  }

  public int getResultCount() {
    return resultCount.get();
  }

  public void setState(int serial, TestState state) {
    int count = serial + 1;
    if (count > resultCount.get())
      resultCount.set(count);
    states.set(serial, state);
  }

  public TestState getState(int serial) {
    return states.get(serial);
  }

}
