// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.utils;
public interface IThunk<T> {

  public void onValue(T t);
  public void onError(Throwable error);

}
