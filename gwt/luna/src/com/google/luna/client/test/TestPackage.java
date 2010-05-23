// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.luna.client.Luna;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.utils.Listeners;
import com.google.luna.client.utils.Promise;
/**
 * A complete collection of tests potentially made up of several different
 * types of test suites.
 */
public class TestPackage {

  /**
   * Listener for events related to loading test case packages.
   */
  public interface IListener {

    /**
     * Invoked when another block of tests has been fetched from the
     * server.  The range of serial numbers of the test block is passed
     * as parameters.
     */
    public void onTestBlockLoaded(int from, int to);

  }

  /**
   * Underlying definition of this package.
   */
  private final Backend.Package data;

  /**
   * The test suites making up this package.
   */
  private final ArrayList<TestSuite> suites;

  private final Listeners<IListener> listeners = new Listeners<IListener>();

  public TestPackage(Backend.Package data) {
    this.data = data;
    this.suites = new ArrayList<TestSuite>();
    JsArray<Backend.Suite> suites = data.getSuites();
    int serialOffset = 0;
    for (int i = 0; i < suites.length(); i++) {
      Backend.Suite suiteData = suites.get(i);
      String type = suiteData.getType();
      ITestCase.IFactory factory = Luna.getTestCaseFactory(type);
      final TestSuite suite = new TestSuite(suiteData, factory, serialOffset);
      this.suites.add(suite);
      suite.addListener(new TestSuite.IListener() {
        @Override
        public void onTestBlockLoaded(int from, int to) {
          onTestSuiteCaseLoaded(suite, from, to);
        }
      });
      serialOffset += suiteData.getCaseCount();
    }
  }

  private void onTestSuiteCaseLoaded(TestSuite suite, int from, int to) {
    int serialOffset = 0;
    for (TestSuite s : suites) {
      if (s == suite) {
        int adjustedFrom = serialOffset + from;
        int adjustedTo = serialOffset + to;
        for (IListener listener : listeners)
          listener.onTestBlockLoaded(adjustedFrom, adjustedTo);
        return;
      } else {
        serialOffset += s.getCaseCount();
      }
    }
  }

  public void addListener(IListener listener) {
    listeners.add(listener);
  }

  public void removeListener(IListener listener) {
    listeners.remove(listener);
  }

  public int getTestCount() {
    int result = 0;
    for (TestSuite suite : suites)
      result += suite.getCaseCount();
    return result;
  }

  public Promise<ITestCase> getCase(int index) {
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
