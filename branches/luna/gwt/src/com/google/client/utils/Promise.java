// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Promise<T> {
  
  private enum State { EMPTY, HAS_VALUE, HAS_ERROR }
  
  private List<Thunk<T>> listeners = new ArrayList<Thunk<T>>();
  private State state = State.EMPTY;
  private T value = null;
  private String error = null;
  
  public Promise() { }
  
  private Promise(T value) {
    setValue(value);
  }
  
  public static <T> Promise<T> from(T value) {
    return new Promise<T>(value);
  }
  
  public boolean onValue(Thunk<T> thunk) {
    switch (state) {
      case EMPTY:
        listeners.add(thunk);
        return false;
      case HAS_VALUE:
        thunk.onValue(value);
        return true;
      case HAS_ERROR:
        thunk.onError(error);
        return true;
    }
    return false;
  }
  
  private List<Thunk<T>> zapListeners() {
    List<Thunk<T>> l = listeners;
    listeners = Collections.emptyList();
    return l;
  }
  
  public void setValue(T value) {
    this.state = State.HAS_VALUE;
    this.value = value;
    for (Thunk<T> t : zapListeners())
      onValue(t);
  }
  
  public void setError(String error) {
    this.state = State.HAS_ERROR;
    this.error = error;
    for (Thunk<T> t : zapListeners())
      onValue(t);
  }
  
}
