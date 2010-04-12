// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Promise<T> {

  private enum State { EMPTY, HAS_VALUE, HAS_ERROR }

  private List<IThunk<T>> listeners = new ArrayList<IThunk<T>>();
  private State state = State.EMPTY;
  private T value = null;
  private Throwable error = null;

  public Promise() { }

  private Promise(T value) {
    setValue(value);
  }

  public static <T> Promise<T> from(T value) {
    return new Promise<T>(value);
  }

  public boolean onValue(IThunk<T> thunk) {
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

  private List<IThunk<T>> zapListeners() {
    List<IThunk<T>> l = listeners;
    listeners = Collections.emptyList();
    return l;
  }

  public void setValue(T value) {
    this.state = State.HAS_VALUE;
    this.value = value;
    for (IThunk<T> t : zapListeners())
      onValue(t);
  }

  public void setError(Throwable error) {
    this.state = State.HAS_ERROR;
    this.error = error;
    for (IThunk<T> t : zapListeners())
      onValue(t);
  }

  public static Promise<Object> defer() {
  	return defer(0);
  }

  public static Promise<Object> defer(int delay) {
  	final Promise<Object> result = new Promise<Object>();
  	schedule(result, delay);
  	return result;
  }

  private static native void schedule(Promise<Object> promise, int delay) /*-{
    setTimeout(function () {
    	promise.@com.google.luna.client.utils.Promise::setValue(Ljava/lang/Object;)(null);
    }, delay);
  }-*/;

}