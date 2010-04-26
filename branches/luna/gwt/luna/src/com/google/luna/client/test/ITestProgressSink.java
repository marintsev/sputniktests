package com.google.luna.client.test;


public interface ITestProgressSink {

  public void testStarted(ITestCase test);
  public void testScriptComplete(ITestCase test);
  public void testDone(ITestCase test);
  public void testSkipped(ITestCase test);
  public void testFailed(ITestCase test, String message);
  public void testPrint(ITestCase test, String message);

}
