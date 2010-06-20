package com.google.luna.client.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.luna.client.Luna;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.rmi.Backend.Case;
import com.google.luna.client.test.data.AbstractTestCase;
import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.utils.Promise;

public class SputnikTestCase extends AbstractTestCase {

  public static IFactory getFactory() {
    return new IFactory() {
      @Override
      public ITestCase create(Case data, int serial) {
        return new SputnikTestCase(data, serial);
      }
    };
  }

  private Promise<String> pDescription;
  private Promise<String> pLabel;
  private String source;
  private Boolean isNegative;
  private IFrameElement iFrame;

  public SputnikTestCase(Backend.Case data, int serial) {
    super(data, serial);
  }

  @Override
  public boolean isNegative() {
    if (isNegative == null)
      isNegative = determineNegativity();
    return isNegative;
  }

  private String getSource() {
    if (this.source == null)
      this.source = buildSource();
    return this.source;
  }

  @Override
  public Promise<String> getDescription() {
    if (this.pDescription == null) {
      String value = extractDescription(getData().getSource());
      this.pDescription = Promise.from(value);
    }
    return this.pDescription;
  }

  @Override
  public Promise<String> getLabel() {
    if (this.pLabel == null) {
      String value = getData().getSection() + "/" + getData().getName();
      this.pLabel = Promise.from(value);
    }
    return this.pLabel;
  }

  private native String extractDescription(String rawSource) /*-{
    var match = /@description:(.*)$/m.exec(rawSource);
    if (!match) {
      return "";
    } else {
      var str = match[1];
      var stripped = /^\s*(.*)\s*;$/.exec(str);
      return stripped ? stripped[1] : str;
    }
  }-*/;

  private String buildSource() {
    String rawSource = getData().getSource();
    rawSource = rawSource.replace("$ERROR", "testFailed");
    rawSource = rawSource.replace("$FAIL", "testFailed");
    rawSource = rawSource.replace("$PRINT", "testPrint");
    return rawSource;
  }

  private boolean determineNegativity() {
    return getData().getSource().contains("@negative");
  }

  @Override
  protected void run(ITestEventHandler runner) {
    iFrame = Document.get().createIFrameElement();
    Luna.getWorkspace().appendChild(iFrame);
    installGlobals(iFrame, runner);
    TestUtils.injectScript(iFrame, "testStarted();");
    String fullSource = getSource() + "\ntestScriptComplete();";
    TestUtils.injectScript(iFrame, fullSource);
    TestUtils.injectScript(iFrame, "testDone();");
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testStarted(ITestEventHandler handler) {
    handler.onAboutToStart();
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testScriptComplete(ITestEventHandler handler) {
    handler.onComplete();
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testFailed(ITestEventHandler runner, String message) {
    runner.onError(message);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testPrint(ITestEventHandler runner, String message) {
    runner.onMessage(message);
  }

  @SuppressWarnings("unused") // Used from JS code
  private void testDone(ITestEventHandler runner) {
    Luna.getWorkspace().removeChild(iFrame);
    this.iFrame = null;
    runner.onDone();
  }

  private native void installGlobals(IFrameElement frame, ITestEventHandler runner) /*-{
    var self = this;
    var global = frame.contentWindow;
    global.testStarted = function () { self.@com.google.luna.client.test.SputnikTestCase::testStarted(Lcom/google/luna/client/test/ITestEventHandler;)(runner); };
    global.testScriptComplete = function () { self.@com.google.luna.client.test.SputnikTestCase::testScriptComplete(Lcom/google/luna/client/test/ITestEventHandler;)(runner); };
    global.testDone = function () { self.@com.google.luna.client.test.SputnikTestCase::testDone(Lcom/google/luna/client/test/ITestEventHandler;)(runner); };
    global.testFailed = function (message) { self.@com.google.luna.client.test.SputnikTestCase::testFailed(Lcom/google/luna/client/test/ITestEventHandler;Ljava/lang/String;)(runner, message); };
    global.testPrint = function (message) { self.@com.google.luna.client.test.SputnikTestCase::testPrint(Lcom/google/luna/client/test/ITestEventHandler;Ljava/lang/String;)(runner, message); };
  }-*/;

}
