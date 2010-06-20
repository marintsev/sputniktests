// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.logic;

import com.google.luna.client.utils.Promise;

public interface ITestControlPanelView {

  public interface IHandler {
    void onStartClicked();
    void onResetClicked();
  }

  public void init(IHandler handler);

  public void setRunProgress(double value);
  public void setLoadProgress(double value);
  public void updateStats(Promise<String> testName, int totalCount,
      int succeededCount, int failedCount);

  public void updateReset(String label, boolean enabled);
  public void updateStart(String label, boolean enabled);

}
