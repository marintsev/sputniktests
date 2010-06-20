// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.widget;

import com.google.luna.client.logic.ITestControlPanelView;



public interface IRunView extends IPageView {

  public enum Mode {
    DISABLED, READY, RUNNING, PAUSED
  }

  public ITestControlPanelView getControlPanel();

  public IResultList getResults();

}
