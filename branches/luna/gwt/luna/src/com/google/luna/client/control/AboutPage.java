package com.google.luna.client.control;

import com.google.luna.client.Luna;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.ui.AboutView;
import com.google.luna.client.ui.IAboutView;
import com.google.luna.client.utils.Thunk;

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
    Luna.getActivePackage().onValue(new Thunk<TestPackage>() {
      public void onValue(TestPackage t) {
        view.setVersion(t.getVersion());
      }
    });
  }

}
