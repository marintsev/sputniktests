package com.google.luna.client.test;
/**
 * A monitor that listens for events during test running.
 */
public interface ITestEventListener {

  /**
   * The test has been loaded but has not yet been started.
   */
  public void testLoaded();

  /**
   * The test is just about to start.
   */
  public void testAboutToStart();

  /**
   * Some error condition occurred.
   */
  public void error(String message);

  /**
   * The test issued an informative message.
   */
  public void message(String message);

  /**
   * The test has run to completion.
   */
  public void testRunToCompletion();

  /**
   * This test is done and will fire no more events.
   */
  public void testDone();

}
