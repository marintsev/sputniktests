package com.google.luna.client.test;


public class TestResults {

  public enum Outcome { EXPECTED, UNEXPECTED, UNEXPECTED_IF_UNSET }

  private final boolean[] results;
  private int resultCount = 0;
  private int expectedCount = 0;
  private int unexpectedCount = 0;

  public TestResults(TestPackage pack) {
    this.results = new boolean[pack.getTestCount()];
  }

  public void setResult(int index, Outcome outcome) {
    if (outcome == Outcome.UNEXPECTED_IF_UNSET) {
      if (index != resultCount)
        setResult(index, Outcome.UNEXPECTED);
      return;
    }
    boolean isExpected = (outcome == Outcome.EXPECTED);
    if (index < resultCount) {
      assert index == resultCount - 1;
      if (!isExpected && results[index]) {
        expectedCount--;
        unexpectedCount++;
        results[index] = false;
      }
    } else {
      assert index == resultCount;
      resultCount = index + 1;
      if (isExpected) expectedCount++;
      else unexpectedCount++;
    }
  }

  public int getExpectedCount() {
    return this.expectedCount;
  }

  public int getUnexpectedCount() {
    return this.unexpectedCount;
  }

}
