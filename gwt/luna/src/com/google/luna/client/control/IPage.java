// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.control;

import com.google.luna.client.ui.IPageView;

/**
 * Abstract interface to a single page of content.
 */
public interface IPage<View extends IPageView> {

  public interface IFactory<View extends IPageView> {
    IPage<View> createPage();
  }

  public View bindView();

  public void init();

}
