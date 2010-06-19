package com.google.luna.client.test;
/**
 * A monitor that listens for events during test running.
 */
public interface ITestEventHandler {

  /**
   * The test has been loaded but has not yet been started.
   */
  public void onLoaded();

  /**
   * The test is just about to start.
   */
  public void onAboutToStart();

  /**
   * Some error condition occurred.
   */
  public void onError(String message);

  /**
   * The test issued an informative message.
   */
  public void onMessage(String message);

  /**
   * The test has run to completion.
   */
  public void onComplete();

  /**
   * This test is done and will fire no more events.
   */
  public void onDone();

}
