package com.google.luna.client.test.data;

import com.google.luna.client.test.ITestEventHandler;
import com.google.luna.client.utils.Promise;

public class FakeTestCase implements ITestCase {

  private final int serial;
  private final boolean isNegative;
  private final Promise<String> description;
  private final Promise<String> label;

  public FakeTestCase(int serial, boolean isNegative, String description,
      String label) {
    this.serial = serial;
    this.isNegative = isNegative;
    this.description = Promise.from(description);
    this.label = Promise.from(label);
  }

  @Override
  public Promise<String> getDescription() {
    return description;
  }

  @Override
  public Promise<String> getLabel() {
    return label;
  }

  @Override
  public int getSerial() {
    return serial;
  }

  @Override
  public boolean isNegative() {
    return isNegative;
  }

  @Override
  public void schedule(ITestEventHandler progress) {
    // ignore.
  }

}
