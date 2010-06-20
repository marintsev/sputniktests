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
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;
import com.google.luna.client.utils.Cookie.Factory;
import com.google.luna.client.widget.IRunView;
import com.google.luna.client.widget.ITestControlPanel;
import com.google.luna.client.widget.RunView;

public class RunPagePresenter implements IPage<IRunView>, ITestControlPanel.IHandler,
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
  private ITestPackage pack;
  private TestScheduler run;
  private int maxTestCaseLoaded = 0;

  private void onReceivedPackage(ITestPackage pack) {
    view.setMode(IRunView.Mode.READY);
    this.pack = pack;
    pack.addListener(new ITestPackage.IListener() {
      @Override
      public void onTestBlockLoaded(int from, int to) {
        onTestCaseBlockLoaded(to);
      }
    });
    Factory factory = Luna.getRootCookieFactory();
    TestProgress progress = new TestProgress(pack, factory.child("results"));
    run = new TestScheduler(progress);
    run.addListener(this);
    final Promise<String> pLabel = new Promise<String>();
    // Fun with thunks!
    progress.peekNextCase().eagerOnValue(new Thunk<ITestCase>() {
      public void onValue(ITestCase t) {
        t.getLabel().eagerOnValue(new Thunk<String>() {
          @Override
          public void onValue(String t) {
            pLabel.setValue(t);
          }
        });
      }
    });
    updateProgressUi(progress.getTestCompleteCount(), pLabel);
  }

  private void onTestCaseBlockLoaded(int value) {
    if (value > maxTestCaseLoaded) {
      maxTestCaseLoaded = value;
      double ratio = ((double) value) / pack.getTestCount();
      view.getController().setLoadProgress(ratio);
    }
  }

  @Override
  public void resetClicked() {

  }

  public void allDone() {
    view.getController().setRunProgress(1);
  }

  private void updateProgressUi(int runCount, Promise<String> currentLabel) {
    double count = pack.getTestCount();
    view.getController().setRunProgress(runCount / count);
    view.getController().updateStats(currentLabel, runCount,
        run.getProgress().getExpectedOutcomeCount(),
        run.getProgress().getUnexpectedOutcomeCount());
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
  public void startClicked() {
    view.setMode(IRunView.Mode.RUNNING);
    run.start();
  }

  @Override
  public void pauseClicked() {
    view.setMode(IRunView.Mode.PAUSED);
    this.run.setPaused(true);
  }

  @Override
  public void resumeClicked() {
    run.setPaused(false);
    view.setMode(IRunView.Mode.RUNNING);
  }

  @Override
  public IRunView bindView() {
    this.view = new RunView();
    view.setMode(IRunView.Mode.DISABLED);
    return this.view;
  }

  @Override
  public void init() {
    view.getController().addHandler(this);
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
