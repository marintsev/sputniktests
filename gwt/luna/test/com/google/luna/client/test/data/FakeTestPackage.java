package com.google.luna.client.test.data;

import java.util.ArrayList;
import java.util.List;

import com.google.luna.client.utils.Promise;

public class FakeTestPackage implements ITestPackage {

  private final List<Promise<ITestCase>> tests;

  public FakeTestPackage(List<? extends ITestCase> tests) {
    this.tests = new ArrayList<Promise<ITestCase>>();
    for (ITestCase test : tests)
      this.tests.add(Promise.from(test));
  }

  @Override
  public void addListener(IListener listener) {
    // ignore
  }

  @Override
  public void removeListener(IListener listener) {
    // ignore
  }

  @Override
  public Promise<ITestCase> getCase(int index) {
    return tests.get(index);
  }

  @Override
  public int getTestCount() {
    return tests.size();
  }

  @Override
  public String getVersion() {
    return null;
  }

  public static FakeTestPackage create(int size) {
    List<ITestCase> tests = new ArrayList<ITestCase>();
    for (int i = 0; i < size; i++)
      tests.add(new FakeTestCase(i, false, "", ""));
    return new FakeTestPackage(tests);
  }

}
