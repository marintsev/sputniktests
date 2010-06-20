package com.google.luna.client.test;

import com.google.luna.client.utils.Cookie;
import com.google.luna.client.utils.EnumVector;
import com.google.luna.client.utils.PersistentEnumVector;
import com.google.luna.client.utils.Cookie.Factory;

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
  private final Cookie<Integer> count;
  private final EnumVector<TestState> states;

  public PersistentTestResults(int testCount, Cookie.Factory cookieFactory) {
    this.states = new PersistentEnumVector<TestState>(TestState.values(),
        testCount, kVectorSegmentSize, cookieFactory.child("state"));
    this.count = cookieFactory.integer("count");
  }

  public int getResultCount() {
    return count.get();
  }

  public void setState(int serial, TestState state) {
    int countIncludingThis = serial + 1;
    if (countIncludingThis > count.get())
      count.set(countIncludingThis);
    states.set(serial, state);
  }

  public TestState getState(int serial) {
    return states.get(serial);
  }

  public void clear() {
    count.clear();
    states.clear();
  }

  public static void clearCookies(int testCount, Factory child) {
    (new PersistentTestResults(testCount, child)).clear();
  }

}
