// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.luna.client.Luna;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class Es5ConformTestController implements ITestController {

  private static final String DESCRIPTION_KEY = "description";

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
  public void start(ITestProgressSink runner, TestCase test) {
    IFrameElement frame = Document.get().createIFrameElement();
    runner.getWorkspace().appendChild(frame);
    installGlobals(frame, runner, test);
    TestUtils.injectScript(frame, test.getSource());
  }

  public void addTest(final ITestProgressSink runner, final TestCase test, String description,
      final JavaScriptObject fun, final JavaScriptObject prereq,
      final IFrameElement frame) {
    test.setAttribute(DESCRIPTION_KEY, description);
    runner.testStarted(test);
    Promise.defer().onValue(new Thunk<Object>() {
      @Override
      public void onValue(Object t) {
        runTest(runner, test, fun, prereq, frame);
      }
    });
  }

  private void testDone(ITestProgressSink runner, TestCase test, IFrameElement frame) {
    runner.getWorkspace().removeChild(frame);
    runner.testDone(test);
  }

  private void testSkipped(ITestProgressSink runner, TestCase test, IFrameElement frame) {
    runner.getWorkspace().removeChild(frame);
    runner.testSkipped(test);
  }

  @Override
  public String buildDescription(TestCase testCase) {
    String description = (String) testCase.getAttribute(DESCRIPTION_KEY);
    return (description == null) ? testCase.getName() : description;
  }

  @Override
  public String getLabel(TestCase test) {
    return test.getSection() + "-" + test.getName();
  }

  private void runTest(ITestProgressSink runner, TestCase test, JavaScriptObject fun,
      JavaScriptObject prereq, IFrameElement frame) {
    boolean skip = true;
    try {
      skip = !shouldExecute(prereq);
    } catch (JavaScriptException jse) {
      Luna.reportError(jse);
      return;
    }
    if (skip) {
      testSkipped(runner, test, frame);
    } else {
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
    }
  }

  private native void installGlobals(IFrameElement frame, ITestProgressSink runner,
      TestCase test) /*-{
    var self = this;
    var global = frame.contentWindow;
    global.sth_addTest = function (name, fun, prereq) { self.@com.google.luna.client.test.Es5ConformTestController::addTest(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/luna/client/test/TestCase;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/dom/client/IFrameElement;)(runner, test, name, fun, prereq, frame); };
    global.fnExists = function (f) { return typeof(f) === 'function'; };
  }-*/;

  private static native boolean shouldExecute(JavaScriptObject prereq) /*-{
    return !prereq || prereq();
  }-*/;

  private static native boolean execute(JavaScriptObject fun) /*-{
    var result = fun();
    return result;
  }-*/;

}
