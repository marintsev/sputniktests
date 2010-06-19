package com.google.luna.client.test;

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
  private final TestEventTranslater eventTranslater = new TestEventTranslater();
  private int nextTest;
  private int currentTest = -1;

  public class TestMonitor {

    private final ITestCase test;
    private final ITestEventListener listener;

    public TestMonitor(ITestCase test) {
      this.test = test;
      this.listener = eventTranslater.getEventListener(test);
    }

    public ITestCase getCase() {
      return this.test;
    }

    public ITestEventListener getListener() {
      return this.listener;
    }

  }

  public TestProgress(ITestPackage pack, Cookie.Factory cookieFactory) {
    this.pack = pack;
    this.results = new PersistentTestResults(pack, cookieFactory.child("results"));
    this.nextTest = results.getResultCount();
  }

  public boolean hasNext() {
    return nextTest < pack.getTestCount();
  }

  public Promise<TestMonitor> getNext() {
    assert hasNext();
    final Promise<TestMonitor> pResult = new Promise<TestMonitor>();
    int serial = nextTest++;
    this.currentTest = serial;
    pack.getCase(serial).onValue(new Thunk<ITestCase>() {
      @Override
      public void onValue(ITestCase test) {
        pResult.setValue(new TestMonitor(test));
      }
    });
    return pResult;
  }

}
