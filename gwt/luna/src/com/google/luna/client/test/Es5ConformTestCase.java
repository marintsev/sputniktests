// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.luna.client.Luna;
import com.google.luna.client.rmi.Backend.Case;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class Es5ConformTestCase extends AbstractTestCase {

  public static IFactory getFactory() {
    return new IFactory() {
      @Override
      public Es5ConformTestCase create(Case data, int serial) {
        return new Es5ConformTestCase(data, serial);
      }
    };
  }

  private Promise<JavaScriptObject> pTestObject;
  private Promise<String> pDescription;
  private Promise<String> pLabel;
  private IFrameElement iFrame = null;
  private boolean isAttached = false;

  public Es5ConformTestCase(Case data, int serial) {
    super(data, serial);
  }

  private Promise<JavaScriptObject> getTestObject() {
    if (pTestObject == null)
      pTestObject = scheduleTestObject();
    return pTestObject;
  }

  private void ensureAttached() {
    if (!isAttached) {
      isAttached = true;
      Luna.getWorkspace().appendChild(iFrame);
    }
  }

  private void ensureDetached() {
    if (isAttached) {
      isAttached = false;
      Luna.getWorkspace().removeChild(iFrame);
    }
  }

  private Promise<JavaScriptObject> scheduleTestObject() {
    assert pTestObject == null;
    pTestObject = new Promise<JavaScriptObject>();
    this.iFrame = Document.get().createIFrameElement();
    ensureAttached();
    installGlobals(pTestObject, iFrame);
    TestUtils.injectScript(iFrame, getData().getSource());
    TestUtils.injectScript(iFrame, "ES5Harness.ensureRegistered();");
    return pTestObject;
  }

  @SuppressWarnings("unused") // Used from JS code
  private void registerTest(Promise<JavaScriptObject> pResult, JavaScriptObject value) {
    pResult.setValue(value);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void ensureRegistered(Promise<JavaScriptObject> pResult, IFrameElement frame) {
    pResult.ensureValueSet(null);
  }

  private native void installGlobals(Promise<JavaScriptObject> pResult, IFrameElement frame) /*-{
    var self = this;
    var global = frame.contentWindow;
    global.ES5Harness = {
      registerTest: function (obj) { self.@com.google.luna.client.test.Es5ConformTestCase::registerTest(Lcom/google/luna/client/utils/Promise;Lcom/google/gwt/core/client/JavaScriptObject;)(pResult, obj); },
      ensureRegistered: function () { self.@com.google.luna.client.test.Es5ConformTestCase::ensureRegistered(Lcom/google/luna/client/utils/Promise;Lcom/google/gwt/dom/client/IFrameElement;)(pResult, frame); }
    };
    var supportsStrict = undefined;
    global.fnSupportsStrict = function () {
      "use strict";
      if (supportsStrict === undefined) {
        try {
          eval('with ({}) {}');
      	  supportsStrict = false;
        } catch (e) {
      	  supportsStrict = true;
        }
      }
      return supportsStrict;
    };
    global.fnExists = function () {
      for (var i = 0; i < arguments.length; i++) {
        if (typeof(arguments[i]) !== "function")
          return false;
       }
       return true;
    };
  }-*/;

  @Override
  public boolean isNegative() {
    return false;
  }

  @Override
  public Promise<String> getDescription() {
    if (pDescription == null) {
      pDescription = new Promise<String>();
      getTestObject().onValue(new Thunk<JavaScriptObject>() {
        @Override
        public void onValue(JavaScriptObject testObject) {
          pDescription.setValue(extractDescription(testObject));
        }
      });
    }
    return pDescription;
  }

  private native String extractDescription(JavaScriptObject testObject) /*-{
    return testObject ? testObject.description : null;
  }-*/;

  @Override
  public Promise<String> getLabel() {
    if (pLabel == null) {
      String value = getData().getSection() + "/" + getData().getName();
      pLabel = Promise.from(value);
    }
    return pLabel;
  }

  @Override
  protected void run(final ITestProgressSink runner) {
    // The test is marked as started first and the test running is
    // forced to wait so the event loop has time to process the UI
    // update showing the test to be running before starting it.
    runner.testStarted(this);
    getTestObject().lazyOnValue(new Thunk<JavaScriptObject>() {
      @Override
      public void onValue(JavaScriptObject testObject) {
        runTest(runner, testObject);
      }
    });
  }

  private void runTest(ITestProgressSink runner, JavaScriptObject testObject) {
    ensureAttached();
    nativeRunTest(runner, testObject);
    ensureDetached();
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testSkipped(ITestProgressSink runner) {
    runner.testSkipped(this);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testScriptComplete(ITestProgressSink runner) {
    runner.testScriptComplete(this);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testFailed(ITestProgressSink runner, String message) {
    runner.testFailed(this, message);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testStarted(ITestProgressSink runner) {
    runner.testStarted(this);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testDone(ITestProgressSink runner) {
    runner.testDone(this);
  }

  private native void nativeRunTest(ITestProgressSink runner, JavaScriptObject testObject) /*-{
    var skip = false;
    try {
      // Skip if there is a precondition and it returns false.
      if (!testObject || (testObject.precondition && !testObject.precondition())) {
        this.@com.google.luna.client.test.Es5ConformTestCase::testSkipped(Lcom/google/luna/client/test/ITestProgressSink;)(runner);
        return;
      }
    } catch (e) {
      // If the precondition fails we count this test as having failed.
      this.@com.google.luna.client.test.Es5ConformTestCase::testFailed(Lcom/google/luna/client/test/ITestProgressSink;Ljava/lang/String;)(runner, String(e));
    }
    try {
      if (!testObject.test())
        this.@com.google.luna.client.test.Es5ConformTestCase::testFailed(Lcom/google/luna/client/test/ITestProgressSink;Ljava/lang/String;)(runner, "");
      this.@com.google.luna.client.test.Es5ConformTestCase::testScriptComplete(Lcom/google/luna/client/test/ITestProgressSink;)(runner);
    } catch (e) {
      this.@com.google.luna.client.test.Es5ConformTestCase::testFailed(Lcom/google/luna/client/test/ITestProgressSink;Ljava/lang/String;)(runner, String(e));
    } finally {
      this.@com.google.luna.client.test.Es5ConformTestCase::testDone(Lcom/google/luna/client/test/ITestProgressSink;)(runner);
    }
  }-*/;

}
