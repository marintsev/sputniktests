package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class CompareView extends PageView implements ICompareView {

  interface IMyUiBinder extends UiBinder<Widget, CompareView> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  public CompareView() {
    this.initWidget(BINDER.createAndBindUi(this));
  }

}
