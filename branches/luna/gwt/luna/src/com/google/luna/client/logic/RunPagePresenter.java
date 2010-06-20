// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.logic;

import com.google.luna.client.Luna;
import com.google.luna.client.control.IPage;
import com.google.luna.client.test.TestOutcome;
import com.google.luna.client.test.TestProgress;
import com.google.luna.client.test.TestScheduler;
import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.test.data.ITestPackage;
import com.google.luna.client.utils.Cookie;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;
import com.google.luna.client.widget.IRunView;
import com.google.luna.client.widget.RunView;
import com.google.luna.client.widget.IRunView.Mode;

public class RunPagePresenter implements IPage<IRunView>, TestControlPanelPresenter.IListener,
    TestScheduler.IListener {

  public static IFactory<IRunView> getFactory() {
    return new IFactory<IRunView>() {
      @Override
      public IPage<IRunView> createPage() {
        return new RunPagePresenter();
      }
    };
  }

  private IRunView view;
  private TestControlPanelPresenter controlPanel;
  private ITestPackage pack;
  private int maxTestCaseLoaded = 0;
  private TestScheduler scheduler;

  private void onReceivedPackage(ITestPackage pack) {
    controlPanel.setMode(IRunView.Mode.READY);
    this.pack = pack;
    pack.addListener(new ITestPackage.IListener() {
      @Override
      public void onTestBlockLoaded(int from, int to) {
        onTestCaseBlockLoaded(to);
      }
    });
    TestProgress progress = createNextProgress(pack, false);
    scheduler = createNextScheduler(progress);
    initializeProgressIndicators();
  }

  private void initializeProgressIndicators() {
    final Promise<String> pLabel;
    if (scheduler.getProgress().getTestCompleteCount() == 0) {
      controlPanel.setMode(Mode.READY);
      pLabel = Promise.from("");
    } else {
      controlPanel.setMode(Mode.PAUSED);
      pLabel = new Promise<String>();
      // Fun with thunks!
      scheduler.getProgress().peekNextCase().eagerOnValue(new Thunk<ITestCase>() {
        public void onValue(ITestCase t) {
          t.getLabel().eagerOnValue(new Thunk<String>() {
            @Override
            public void onValue(String t) {
              pLabel.setValue(t);
            }
          });
        }
      });
    }
    updateProgressUi(scheduler.getProgress().getTestCompleteCount(),
        pLabel);
  }

  private TestProgress createNextProgress(ITestPackage pack, boolean clear) {
    Cookie.Factory factory = Luna.getRootCookieFactory();
    if (clear)
      TestProgress.clearCookies(pack, factory);
    return new TestProgress(pack, factory);
  }

  private TestScheduler createNextScheduler(TestProgress progress) {
    TestScheduler scheduler = new TestScheduler(progress);
    scheduler.addListener(this);
    return scheduler;
  }

  private void onTestCaseBlockLoaded(int value) {
    if (value > maxTestCaseLoaded) {
      maxTestCaseLoaded = value;
      double ratio = ((double) value) / pack.getTestCount();
      view.getControlPanel().setLoadProgress(ratio);
    }
  }

  public void allDone() {
    view.getControlPanel().setRunProgress(1);
  }

  private void updateProgressUi(int runCount, Promise<String> currentLabel) {
    double count = pack.getTestCount();
    view.getControlPanel().setRunProgress(runCount / count);
    view.getControlPanel().updateStats(currentLabel, runCount,
        scheduler.getProgress().getExpectedOutcomeCount(),
        scheduler.getProgress().getUnexpectedOutcomeCount());
  }

  public void testStarting(ITestCase test) {
    updateProgressUi(test.getSerial(), test.getLabel());
  }

  @Override
  public void testDone(TestOutcome outcome) {
    if (!outcome.wasExpected())
      view.getResults().addResult(outcome);
  }

  @Override
  public void onResetClicked() {
    TestProgress progress = createNextProgress(this.pack, true);
    scheduler = createNextScheduler(progress);
    initializeProgressIndicators();
  }

  @Override
  public void onStartClicked() {
    controlPanel.setMode(IRunView.Mode.RUNNING);
    scheduler.start();
  }

  @Override
  public void onPauseClicked() {
    controlPanel.setMode(IRunView.Mode.PAUSED);
    this.scheduler.setPaused(true);
  }

  @Override
  public void onResumeClicked() {
    scheduler.setPaused(false);
    controlPanel.setMode(IRunView.Mode.RUNNING);
  }

  @Override
  public IRunView bindView() {
    this.view = new RunView();
    this.controlPanel = new TestControlPanelPresenter(view.getControlPanel());
    controlPanel.init();
    controlPanel.setMode(IRunView.Mode.DISABLED);
    return this.view;
  }

  @Override
  public void init() {
    controlPanel.addListener(this);
    fetchActivePackage();
  }

  private void fetchActivePackage() {
    Luna.getActivePackage().onValue(new Thunk<ITestPackage>() {
      public void onValue(ITestPackage pack) {
        onReceivedPackage(pack);
      }
    });
  }

}
