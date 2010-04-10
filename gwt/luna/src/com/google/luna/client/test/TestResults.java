package com.google.luna.client.test;


public class TestResults {

	public enum Outcome { EXPECTED, UNEXPECTED };

	private final boolean[] results;
	private int resultCount = 0;
	private int expectedCount = 0;
	private int unexpectedCount = 0;

	public TestResults(TestPackage pack) {
		this.results = new boolean[pack.getTestCount()];
	}

	public void setResult(int index, Outcome outcome) {
		boolean isExpected = (outcome == Outcome.EXPECTED);
		if (index < resultCount) {
			if (!isExpected && results[index]) {
				expectedCount--;
				unexpectedCount++;
				results[index] = false;
			}
		} else {
			results[index] = isExpected;
			resultCount = Math.max(index + 1, resultCount);
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
