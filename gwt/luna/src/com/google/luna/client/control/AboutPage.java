package com.google.luna.client.control;

import com.google.luna.client.Luna;
import com.google.luna.client.test.data.ITestPackage;
import com.google.luna.client.utils.Thunk;
import com.google.luna.client.widget.AboutView;
import com.google.luna.client.widget.IAboutView;

public class AboutPage implements IPage<IAboutView> {

  public static IFactory<IAboutView> getFactory() {
    return new IFactory<IAboutView>() {
      @Override
      public IPage<IAboutView> createPage() {
        return new AboutPage();
      }
    };
  }

  private IAboutView view;

  @Override
  public IAboutView bindView() {
    this.view = new AboutView();
    return this.view;
  }

  @Override
  public void init() {
    Luna.getActivePackage().onValue(new Thunk<ITestPackage>() {
      public void onValue(ITestPackage t) {
        view.setVersion(t.getVersion());
      }
    });
  }

}
