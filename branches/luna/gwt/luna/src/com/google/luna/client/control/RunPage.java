// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.control;

import com.google.luna.client.Luna;
import com.google.luna.client.test.TestCase;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.test.TestRun;
import com.google.luna.client.ui.IRunView;
import com.google.luna.client.ui.ITestControlPanel;
import com.google.luna.client.ui.RunView;
import com.google.luna.client.utils.Thunk;

public class RunPage implements IPage<IRunView>, ITestControlPanel.IHandler,
TestRun.IListener {

  public static IFactory<IRunView> getFactory() {
    return new IFactory<IRunView>() {
      @Override
      public IPage<IRunView> createPage() {
        return new RunPage();
      }
    };
  }

  private IRunView view;
  private TestPackage pack;
  private TestRun run;

  private void onReceivedPackage(TestPackage pack) {
    view.setMode(IRunView.Mode.READY);
    this.pack = pack;
    pack.addLoadListener(new TestPackage.ILoadListener() {
      @Override
      public void hasLoaded(int max) {
        maxTestCaseLoaded(max);
      }
    });
  }

  private void maxTestCaseLoaded(int value) {
    double ratio = ((double) value) / pack.getTestCount();
    view.getController().setLoadProgress(ratio);
  }

  @Override
  public void resetClicked() {

  }

  public void allDone() {
    view.getController().setRunProgress(1);
  }

  public void testStarted(TestCase test) {
    int runCount = test.getSerial();
    double count = pack.getTestCount();
    view.getController().setRunProgress(runCount / count);
    String name = test.getSection() + "/" + test.getName();
    view.getController().updateStats(name, runCount, run.getResults().getExpectedCount(),
        run.getResults().getUnexpectedCount());
  }

  @Override
  public void startClicked() {
    view.setMode(IRunView.Mode.RUNNING);
    this.run = new TestRun(pack, this, view.getWorkspace());
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
    Luna.getActivePackage().onValue(new Thunk<TestPackage>() {
      public void onValue(TestPackage pack) {
        onReceivedPackage(pack);
      }
    });
  }

}
