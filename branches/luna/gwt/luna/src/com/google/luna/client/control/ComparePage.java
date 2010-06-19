package com.google.luna.client.control;

import com.google.luna.client.widget.CompareView;
import com.google.luna.client.widget.ICompareView;

public class ComparePage implements IPage<ICompareView> {

  public static IFactory<ICompareView> getFactory() {
    return new IFactory<ICompareView>() {
      @Override
      public IPage<ICompareView> createPage() {
        return new ComparePage();
      }
    };
  }

  private ICompareView view;

  @Override
  public ICompareView bindView() {
    this.view = new CompareView();
    return this.view;
  }

  @Override
  public void init() {
  }


}
