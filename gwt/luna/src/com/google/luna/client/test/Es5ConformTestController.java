// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class Es5ConformTestController implements ITestController {

  public static Factory getFactory() {
    return new Factory() {
      @Override
      public ITestController create() {
        return new Es5ConformTestController();
      }
    };
  }

  @Override
  public String buildSource(TestCase test) {
    return test.getRawSource();
  }

  @Override
  public boolean isNegative(TestCase test) {
    return false;
  }

  @Override
  public void start(ITestRun runner, TestCase test) {
    IFrameElement frame = Document.get().createIFrameElement();
    runner.getWorkspace().appendChild(frame);
    installGlobals(frame, runner, test);
    TestUtils.injectScript(frame, test.getSource());
  }

  public void addTest(final ITestRun runner, final TestCase test, String name,
      final JavaScriptObject fun, final JavaScriptObject prereq,
      final IFrameElement frame) {
    runner.testStarted(test);
    Promise.defer().onValue(new Thunk<Object>() {
      @Override
      public void onValue(Object t) {
        runTest(runner, test, fun, prereq, frame);
      }
    });
  }

  private void testDone(ITestRun runner, TestCase test, IFrameElement frame) {
    runner.getWorkspace().removeChild(frame);
    runner.testDone(test);
  }

  private void testSkipped(ITestRun runner, TestCase test, IFrameElement frame) {
    runner.getWorkspace().removeChild(frame);
    runner.testSkipped(test);
  }

  private void runTest(ITestRun runner, TestCase test, JavaScriptObject fun,
      JavaScriptObject prereq, IFrameElement frame) {
    if (shouldExecute(prereq)) {
      try {
        if (execute(fun)) {
          runner.testScriptComplete(test);
        } else {
          runner.testFailed(test, "Failed");
        }
      } catch (JavaScriptException jse) {
        runner.testFailed(test, jse.getMessage());
      }
      testDone(runner, test, frame);
    } else {
      testSkipped(runner, test, frame);
    }
  }

  private native void installGlobals(IFrameElement frame, ITestRun runner,
      TestCase test) /*-{
    var self = this;
    var global = frame.contentWindow;
    global.sth_addTest = function (name, fun, prereq) { self.@com.google.luna.client.test.Es5ConformTestController::addTest(Lcom/google/luna/client/test/ITestRun;Lcom/google/luna/client/test/TestCase;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/dom/client/IFrameElement;)(runner, test, name, fun, prereq, frame); };
    global.fnExists = function (f) { return typeof(f) === 'function'; };
  }-*/;

  private static native boolean shouldExecute(JavaScriptObject prereq) /*-{
    try {
      return !prereq || prereq();
    } catch (e) {
      return false;
    }
  }-*/;

  private static native boolean execute(JavaScriptObject fun) /*-{
    var result = fun();
    return result;
  }-*/;

}
