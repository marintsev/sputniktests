package com.google.luna.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.luna.client.logic.ILogView;
import com.google.luna.client.logic.ToplevelPresenter;

public class MobileToplevel extends ToplevelWidget {

  @UiTemplate("MobileToplevel.ui.xml")
  interface IMyUiBinder extends UiBinder<Widget, MobileToplevel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  protected MobileToplevel(ToplevelPresenter presenter, IPageView contents) {
    super(presenter, contents);
    initWidget(BINDER.createAndBindUi(this));
  }
  
  @Override
  public void setLogVisible(boolean shouldShowLog) {

  }
  
  @Override
  public ILogView getLog() {
    return null;
  }

}
 