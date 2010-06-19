// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.widget;



public interface IRunView extends IPageView {

  public enum Mode {
    DISABLED, READY, RUNNING, PAUSED
  }

  public ITestControlPanel getController();

  public void setMode(Mode mode);

  public IResultList getResults();

}
