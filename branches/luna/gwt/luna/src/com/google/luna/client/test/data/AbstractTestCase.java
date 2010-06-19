// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test.data;

import com.google.luna.client.rmi.Backend;
import com.google.luna.client.test.ITestProgressSink;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public abstract class AbstractTestCase implements ITestCase {

  private final Backend.Case data;
  private final int serial;

  public AbstractTestCase(Backend.Case data, int serial) {
    this.data = data;
    this.serial = serial;
  }

  @Override
  public int getSerial() {
    return this.serial;
  }

  @Override
  public void schedule(final ITestProgressSink progress) {
    Promise.defer().onValue(new Thunk<Object>() {
      @Override
      public void onValue(Object t) {
        AbstractTestCase.this.run(progress);
      }
    });
  }

  protected abstract void run(ITestProgressSink runner);

  protected Backend.Case getData() {
    return this.data;
  }

}
