package com.google.luna.client.logic;

import com.google.luna.client.Log;
import com.google.luna.client.widget.ToplevelWidget;

public class ToplevelPresenter {

  private boolean isShowingLog = Log.isEnabled();
  private IToplevel view;

  public void onLogClicked() {
    boolean shouldShowLog = !isShowingLog;
    view.setLogVisible(shouldShowLog);
    Log.setEnabled(shouldShowLog);
    isShowingLog = shouldShowLog;
  }

  public void init() {
    view.setLogVisible(isShowingLog);
    view.init();
  }

  public void setView(ToplevelWidget view) {
    this.view = view;
  }

  public ILogView getLog() {
    return view == null ? null : view.getLog();
  }

}
