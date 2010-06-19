package com.google.luna.client.control;

import com.google.luna.client.widget.AboutView;
import com.google.luna.client.widget.IPageView;

public class ManagePage implements IPage<IPageView> {

  public static IFactory<IPageView> getFactory() {
    return new IFactory<IPageView>() {
      @Override
      public IPage<IPageView> createPage() {
        return new ManagePage();
      }
    };
  }

  @Override
  public IPageView bindView() {
    return new AboutView();
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

}
