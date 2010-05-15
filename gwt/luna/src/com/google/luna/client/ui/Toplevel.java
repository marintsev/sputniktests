// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.luna.client.Luna;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.ui.Toplevel.Resources.MobileModeCss;
import com.google.luna.client.utils.Thunk;

public abstract class Toplevel extends Composite {

  public interface Resources extends ClientBundle {

    public interface MobileModeCss extends CssResource {
      String desktop();
      String mobile();
    }

    @Source("MobileMode.css")
    public MobileModeCss css();

  }

  private static Resources RESOURCES = GWT.create(Resources.class);

  public static Resources getResources() {
    return RESOURCES;
  }

  public static String getMobileMode(MobileModeCss css) {
    return Luna.getParameters().isMobile() ? css.mobile() : css.desktop();
  }

  @UiField Label subtitle;
  @UiField TabButton aboutButton;
  @UiField TabButton runButton;
  @UiField TabButton compareButton;
  @UiField(provided=true) final Resources resources = getResources();
  @UiField(provided=true) final PageView contents;

  protected Toplevel(IPageView contents) {
    this.contents = (PageView) contents;
  }

  public void initUi() {
    setVersion("#");
  }

  public void init() {
    this.initUi();
    Luna.getActivePackage().onValue(new Thunk<TestPackage>() {
      @Override
      public void onValue(TestPackage pack) {
        setVersion(pack.getVersion());
      }
    });
  }

  private void setVersion(String version) {
    String subtitleText = Luna.getMessages().subtitle(version);
    if (Luna.isDevelMode())
      subtitleText += " (DEVEL)";
    subtitle.setText(subtitleText);
  }

  public static Toplevel create(IPageView page) {
    if (Luna.getParameters().isMobile()) {
      return new MobileToplevel(page);
    } else {
      return new DesktopToplevel(page);
    }
  }

}
