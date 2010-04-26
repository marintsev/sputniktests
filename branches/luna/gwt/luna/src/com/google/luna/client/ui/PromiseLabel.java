package com.google.luna.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class PromiseLabel extends Label {

  private Promise<String> pText = null;

  public void setText(final Promise<String> pText) {
    this.pText = pText;
    pText.eagerOnValue(new Thunk<String>() {
      @Override
      public void onValue(String value) {
        if (pText == PromiseLabel.this.pText) {
          setText(value);
        }
      }
    });
  }

  @Override
  public void setText(String value) {
    pText = null;
    super.setText(value);
  }

}
