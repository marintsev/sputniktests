// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import com.google.client.resources.Bundle;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;

public class IndexRenderer extends MainPageRenderer {

  @Override
  public void renderContents(Panel root) {
  	root.add(new HTML(Bundle.INSTANCE.about().getText()));
  }

}
