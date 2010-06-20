package com.google.luna.client.logic;

import com.google.luna.client.Log;
import com.google.luna.client.Log.Component;
import com.google.luna.client.utils.Listeners;
import com.google.luna.client.widget.IRunView;

public class TestControlPanelPresenter implements ITestControlPanelView.IHandler {

  public interface IListener {
    public void onResetClicked();
    public void onStartClicked();
    public void onPauseClicked();
    public void onResumeClicked();
  }

  private final ITestControlPanelView view;
  private IRunView.Mode currentMode = IRunView.Mode.DISABLED;
  private final Listeners<IListener> listeners = new Listeners<IListener>();

  public TestControlPanelPresenter(ITestControlPanelView view) {
    this.view = view;
  }

  public void init() {
    view.init(this);
  }

  public void addListener(IListener listener) {
    listeners.add(listener);
  }

  public void removeListener(IListener listener) {
    listeners.remove(listener);
  }

  public void setMode(IRunView.Mode mode) {
    this.currentMode = mode;
    if (Log.isEnabled(Component.CONTROL_MODE)) {
      Log.addMessage(Component.CONTROL_MODE, "Now in mode " + mode);
    }
    switch (mode) {
    case DISABLED:
      view.updateReset("Reset", false);
      view.updateStart("Start", false);
      break;
    case READY:
      view.updateReset("Reset", false);
      view.updateStart("Start", true);
      break;
    case RUNNING:
      view.updateReset("Reset", false);
      view.updateStart("Pause", true);
      break;
    case PAUSED:
      view.updateReset("Reset", true);
      view.updateStart("Resume", true);
      break;
    }
  }

  @Override
  public void onStartClicked() {
    switch (currentMode) {
    case RUNNING:
      firePauseClicked();
      break;
    case PAUSED:
      fireResumeClicked();
      break;
    default:
      fireStartClicked();
    break;
    }
  }

  @Override
  public void onResetClicked() {
    fireResetClicked();
  }

  private void fireStartClicked() {
    for (IListener handler : listeners)
      handler.onStartClicked();
  }

  private void firePauseClicked() {
    for (IListener handler : listeners)
      handler.onPauseClicked();
  }

  private void fireResumeClicked() {
    for (IListener handler : listeners)
      handler.onResumeClicked();
  }

  private void fireResetClicked() {
    for (IListener handler : listeners)
      handler.onResetClicked();
  }

}
