package com.google.luna.client.test;

import com.google.gwt.dom.client.Element;

public interface ITestProgressSink {

	public void testStarted(TestCase test);
	public void testScriptComplete(TestCase test);
	public void testDone(TestCase test);
	public void testSkipped(TestCase test);
	public void testFailed(TestCase test, String message);
	public void testPrint(TestCase test, String message);

	public Element getWorkspace();

}
