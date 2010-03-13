// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import com.google.client.rmi.Backend;

public abstract class MainPageRenderer {
  
  private Backend backend;
  private Sputnik sputnik;
  
  public void initialize(Backend backend, Sputnik sputnik) {
    this.backend = backend;
    this.sputnik = sputnik;
  }
  
  protected Backend getBackend() {
    return this.backend;
  }
  
  protected Sputnik getSputnik() {
    return this.sputnik;
  }
  
  public abstract void render();

}
