package com.google.luna.client.test.data;

import com.google.luna.client.utils.Promise;

/**
 * A complete collection of tests potentially made up of several different
 * types of test suites.
 */
public interface ITestPackage {

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

  public void addListener(IListener listener);

  public void removeListener(IListener listener);

  public int getTestCount();

  public Promise<ITestCase> getCase(int index);

  public String getVersion();

}
