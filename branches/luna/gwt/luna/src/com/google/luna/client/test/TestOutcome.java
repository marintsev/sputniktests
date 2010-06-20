package com.google.luna.client.test;

import com.google.luna.client.test.data.ITestCase;

import java.util.List;

public class TestOutcome {

  public enum Status { EXPECTED, UNEXPECTED }

  private final ITestCase test;
  private final Status status;
  private final String error;
  private final List<String> messages;

  public TestOutcome(ITestCase test, Status status, String error, List<String> messages) {
    this.test = test;
    this.status = status;
    this.error = error;
    this.messages = messages;
  }

  public boolean wasExpected() {
    return status == Status.EXPECTED;
  }

  public Status getStatus() {
    return this.status;
  }

  public String getError() {
    return this.error;
  }

  public List<String> getMessages() {
    return this.messages;
  }

  public int getSerial() {
    return test.getSerial();
  }

  public ITestCase getTest() {
    return this.test;
  }

}
