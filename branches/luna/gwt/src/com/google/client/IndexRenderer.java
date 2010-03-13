// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import com.google.client.rmi.Backend;
import com.google.client.rmi.Backend.Suite;
import com.google.gwt.user.client.Window;

public class IndexRenderer extends MainPageRenderer {

  @Override public void render() {
    getBackend().getActiveSuite("sputnik", ".").onValue(getSputnik().new SimpleThunk<Backend.Suite>() {
      @Override
      public void onValue(Suite t) {
        Window.alert(t.toString());
      }
    });
  }

}
