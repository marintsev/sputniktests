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
  protected void run(ITestProgressSink runner) {
    IFrameElement frame = Document.get().createIFrameElement();
    Luna.getWorkspace().appendChild(frame);
    installGlobals(frame, runner);
    TestUtils.injectScript(frame, "testStarted();");
    String fullSource = getSource() + "\ntestScriptComplete();";
    TestUtils.injectScript(frame, fullSource);
    TestUtils.injectScript(frame, "testDone();");
  }

  public void testStarted(ITestProgressSink runner) {
    runner.testStarted(this);
  }

  public void testScriptComplete(ITestProgressSink runner) {
    runner.testScriptComplete(this);
  }

  public void testFailed(ITestProgressSink runner, String message) {
    runner.testFailed(this, message);
  }

  public void testPrint(ITestProgressSink runner, String message) {
    runner.testPrint(this, message);
  }

  public void testDone(ITestProgressSink runner, IFrameElement frame) {
    Luna.getWorkspace().removeChild(frame);
    runner.testDone(this);
  }

  private native void installGlobals(IFrameElement frame, ITestProgressSink runner) /*-{
    var self = this;
    var global = frame.contentWindow;
    global.testStarted = function () { self.@com.google.luna.client.test.SputnikTestCase::testStarted(Lcom/google/luna/client/test/ITestProgressSink;)(runner); };
    global.testScriptComplete = function () { self.@com.google.luna.client.test.SputnikTestCase::testScriptComplete(Lcom/google/luna/client/test/ITestProgressSink;)(runner); };
    global.testDone = function () { self.@com.google.luna.client.test.SputnikTestCase::testDone(Lcom/google/luna/client/test/ITestProgressSink;Lcom/google/gwt/dom/client/IFrameElement;)(runner, frame); };
    global.testFailed = function (message) { self.@com.google.luna.client.test.SputnikTestCase::testFailed(Lcom/google/luna/client/test/ITestProgressSink;Ljava/lang/String;)(runner, message); };
    global.testPrint = function (message) { self.@com.google.luna.client.test.SputnikTestCase::testPrint(Lcom/google/luna/client/test/ITestProgressSink;Ljava/lang/String;)(runner, message); };
  }-*/;

}
