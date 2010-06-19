package com.google.luna.client.logic;

public interface IToplevel {

  void setLogVisible(boolean shouldShowLog);
  
  ILogView getLog();

  void init();
  
}
