// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.core.client.JsArray;
import com.google.luna.client.Luna;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.utils.Promise;

import java.util.ArrayList;

public class TestPackage {

  public interface ILoadListener {
    public void hasLoaded(int max);
  }

  private final Backend.Package data;
  private final ArrayList<TestSuite> suites;
  private final ArrayList<ILoadListener> listeners = new ArrayList<ILoadListener>();
  private int maxLoaded = 0;

  public TestPackage(Backend.Package data) {
    this.data = data;
    this.suites = new ArrayList<TestSuite>();

    TestSuite.ILoadListener loadListener = new TestSuite.ILoadListener() {
      @Override
      public void hasLoaded(TestSuite suite, int max) {
        testSuiteCaseLoaded(suite, max);
      }
    };

    JsArray<Backend.Suite> suites = data.getSuites();
    int serialOffset = 0;
    for (int i = 0; i < suites.length(); i++) {
      Backend.Suite suiteData = suites.get(i);
      String type = suiteData.getType();
      ITestController.Factory factory = Luna.getTestControllerFactory(type);
      ITestController controller = factory.create();
      TestSuite suite = new TestSuite(suiteData, controller, serialOffset);
      this.suites.add(suite);
      suite.addLoadListener(loadListener);
      serialOffset += suiteData.getCaseCount();
    }
  }

  private void testSuiteCaseLoaded(TestSuite suite, int max) {
    int base = 0;
    for (TestSuite s : suites) {
      if (suite == s) {
        int value = base + max;
        if (value > maxLoaded) {
          for (ILoadListener listener : listeners)
            listener.hasLoaded(value);
          return;
        }
      } else {
        base += s.getCaseCount();
      }
    }
  }

  public void addLoadListener(ILoadListener listener) {
    listeners.add(listener);
  }

  public void removeLoadListener(ILoadListener listener) {
    listeners.remove(listener);
  }

  public int getTestCount() {
    int result = 0;
    for (TestSuite suite : suites)
      result += suite.getCaseCount();
    return result;
  }

  public Promise<TestCase> getCase(int index) {
    for (TestSuite suite : suites) {
      int count = suite.getCaseCount();
      if (index < count)
        return suite.getCase(index);
      index -= count;
    }
    return null;
  }

  public String getVersion() {
    return data.getVersion();
  }

}
