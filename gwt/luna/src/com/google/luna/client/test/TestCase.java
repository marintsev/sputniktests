// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import com.google.luna.client.rmi.Backend;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

import java.util.HashMap;

public class TestCase {

  private enum Negativity { UNKNOWN, NEGATIVE, POSITIVE }

  private final Backend.Case data;
  private final int serial;
  private final ITestController controller;

  private String source = null;
  private Negativity negativity = Negativity.UNKNOWN;
  private String description = null;
  private HashMap<String, Object> attribs = null;

  public TestCase(Backend.Case data, int serial, ITestController controller) {
    this.data = data;
    this.serial = serial;
    this.controller = controller;
  }

  public void setAttribute(String key, Object value) {
    if (attribs == null)
      attribs = new HashMap<String, Object>();
    attribs.put(key, value);
  }

  public Object getAttribute(String key) {
    if (attribs == null)
      return null;
    return attribs.get(key);
  }

  public int getSerial() {
    return this.serial;
  }

  public String getName() {
    return data.getName();
  }

  public String getSection() {
    return data.getSection();
  }

  public String getLabel() {
    return controller.getLabel(this);
  }

  public String getSource() {
    if (source == null)
      source = controller.buildSource(this);
    return source;
  }

  public String getDescription() {
    if (description == null)
      description = controller.buildDescription(this);
    return description;
  }

  public String getRawSource() {
    return data.getSource();
  }

  public boolean isNegative() {
    if (negativity == Negativity.UNKNOWN) {
      negativity = controller.isNegative(this)
      ? Negativity.NEGATIVE
          : Negativity.POSITIVE;
    }
    return negativity == Negativity.NEGATIVE;
  }

  @Override
  public String toString() {
    return "test case { id: #" + this.serial + ", name: " + data.getName() +
    ", section: " + data.getSection() + " }";
  }

  public void schedule(final TestRun runner) {
    Promise.defer().onValue(new Thunk<Object>() {
      @Override
      public void onValue(Object t) {
        controller.start(runner, TestCase.this);
      }
    });
  }

}
