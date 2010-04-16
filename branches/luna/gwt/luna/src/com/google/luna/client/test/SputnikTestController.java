// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;

public class SputnikTestController implements ITestController {

  public static Factory getFactory() {
    return new Factory() {
      @Override
      public ITestController create() {
        return new SputnikTestController();
      }
    };
  }

  @Override
  public String buildDescription(TestCase testCase) {
    return getDescription(testCase.getRawSource());
  }

  private native String getDescription(String rawSource) /*-{
    var match = /@description:(.*)$/m.exec(rawSource);
    if (!match) {
      return "";
    } else {
      var str = match[1];
      var stripped = /^\s*(.*)\s*;$/.exec(str);
      return stripped ? stripped[1] : str;
    }
  }-*/;

  @Override
  public String getLabel(TestCase test) {
    return test.getSection() + "/" + test.getName();
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
  public void start(ITestProgressSink runner, TestCase test) {
    IFrameElement frame = Document.get().createIFrameElement();
    runner.getWorkspace().appendChild(frame);
    installGlobals(frame, runner, test);
    TestUtils.injectScript(frame, "testStarted();");
    String fullSource = test.getSource() + "\ntestScriptComplete();";
    TestUtils.injectScript(frame, fullSource);
    TestUtils.injectScript(frame, "testDone();");
  }

  public void testStarted(ITestProgressSink runner, TestCase test) {
    runner.testStarted(test);
  }

  public void testScriptComplete(ITestProgressSink runner, TestCase test) {
    runner.testScriptComplete(test);
  }

  public void testFailed(ITestProgressSink runner, TestCase test, String message) {
    runner.testFailed(test, message);
  }

  public void testPrint(ITestProgressSink runner, TestCase test, String message) {
    runner.testPrint(test, message);
  }

  public void testDone(ITestProgressSink runner, TestCase test, IFrameElement frame) {
    runner.getWorkspace().removeChild(frame);
    runner.testDone(test);
  }

  private native void installGlobals(IFrameElement frame, ITestProgressSink runner,
      TestCase test) /*-{
	var self = this;
	var global = frame.contentWindow;
  	global.testStarted = function () { self.@com.google.luna.client.test.SputnikTestController::testStarted(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/luna/client/test/TestCase;)(runner, test); };
  	global.testScriptComplete = function () { self.@com.google.luna.client.test.SputnikTestController::testScriptComplete(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/luna/client/test/TestCase;)(runner, test); };
  	global.testDone = function () { self.@com.google.luna.client.test.SputnikTestController::testDone(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/luna/client/test/TestCase;Lcom/google/gwt/dom/client/IFrameElement;)(runner, test, frame); };
  	global.testFailed = function (message) { self.@com.google.luna.client.test.SputnikTestController::testFailed(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/luna/client/test/TestCase;Ljava/lang/String;)(runner, test, message); };
  	global.testPrint = function (message) { self.@com.google.luna.client.test.SputnikTestController::testPrint(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/luna/client/test/TestCase;Ljava/lang/String;)(runner, test, message); };
  }-*/;

}
