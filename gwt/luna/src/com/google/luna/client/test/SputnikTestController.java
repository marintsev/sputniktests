// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.luna.client.Luna;

public class SputnikTestController implements ITestController {

  public static Factory getFactory() {
    return new Factory() {
      @Override
      public ITestController create() {
        return new SputnikTestController();
      }
    };
  }

  private SputnikTestController() {
    Luna.reportError(new RuntimeException());
  }

  public String buildSource(TestCase test) {
    String rawSource = test.getRawSource();
    rawSource = rawSource.replace("$ERROR", "testFailed");
    rawSource = rawSource.replace("$FAIL", "testFailed");
    rawSource = rawSource.replace("$PRINT", "testPrint");
    return rawSource;
  }

  @Override
  public boolean isNegative(TestCase test) {
    return test.getRawSource().contains("@negative");
  }

  @Override
  public void start(ITestRun runner, TestCase test) {
    IFrameElement frame = Document.get().createIFrameElement();
    runner.getWorkspace().appendChild(frame);
    installCallbacks(frame, runner, test);
    TestUtils.injectScript(frame, "testStarted();");
    String fullSource = test.getSource() + "\ntestScriptComplete();";
    TestUtils.injectScript(frame, fullSource);
    TestUtils.injectScript(frame, "testDone();");
  }

  public void testStarted(ITestRun runner, TestCase test) {
    runner.testStarted(test);
  }

  public void testScriptComplete(ITestRun runner, TestCase test) {
    runner.testScriptComplete(test);
  }

  public void testFailed(ITestRun runner, TestCase test, String message) {
    runner.testFailed(test, message);
  }

  public void testPrint(ITestRun runner, TestCase test, String message) {
    runner.testPrint(test, message);
  }

  public void testDone(ITestRun runner, TestCase test, IFrameElement frame) {
    runner.getWorkspace().removeChild(frame);
    runner.testDone(test);
  }

  private native void installCallbacks(IFrameElement frame, ITestRun runner,
      TestCase test) /*-{
	var self = this;
	var global = frame.contentWindow;
  	global.testStarted = function () { self.@com.google.luna.client.test.SputnikTestController::testStarted(Lcom/google/luna/client/test/ITestRun;Lcom/google/luna/client/test/TestCase;)(runner, test); };
  	global.testScriptComplete = function () { self.@com.google.luna.client.test.SputnikTestController::testScriptComplete(Lcom/google/luna/client/test/ITestRun;Lcom/google/luna/client/test/TestCase;)(runner, test); };
  	global.testDone = function () { self.@com.google.luna.client.test.SputnikTestController::testDone(Lcom/google/luna/client/test/ITestRun;Lcom/google/luna/client/test/TestCase;Lcom/google/gwt/dom/client/IFrameElement;)(runner, test, frame); };
  	global.testFailed = function (message) { self.@com.google.luna.client.test.SputnikTestController::testFailed(Lcom/google/luna/client/test/ITestRun;Lcom/google/luna/client/test/TestCase;Ljava/lang/String;)(runner, test, message); };
  	global.testPrint = function (message) { self.@com.google.luna.client.test.SputnikTestController::testPrint(Lcom/google/luna/client/test/ITestRun;Lcom/google/luna/client/test/TestCase;Ljava/lang/String;)(runner, test, message); };
  }-*/;

}
