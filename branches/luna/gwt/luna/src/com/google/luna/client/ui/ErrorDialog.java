// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.user.client.ui.DialogBox;

public class ErrorDialog extends DialogBox {

  public ErrorDialog(Throwable error) {
    this.setText(error.getMessage());
    this.add(new ErrorDialogBody(error));
  }

}
