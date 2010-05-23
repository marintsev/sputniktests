// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class FramePanel extends Composite implements HasWidgets {

  interface IMyUiBinder extends UiBinder<Widget, FramePanel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  public interface IResources extends ClientBundle {

    @Source("gfx/ne.png")
    ImageResource northEast();

    @Source("gfx/nw.png")
    ImageResource northWest();

    @Source("gfx/sw.png")
    ImageResource southWest();

    @Source("gfx/se.png")
    ImageResource southEast();

    @Source("gfx/n.png")
    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource north();

    @Source("gfx/e.png")
    @ImageOptions(repeatStyle=RepeatStyle.Vertical)
    ImageResource east();

    @Source("gfx/s.png")
    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource south();

    @Source("gfx/w.png")
    @ImageOptions(repeatStyle=RepeatStyle.Vertical)
    ImageResource west();

    @Source("gfx/ne_emph.png")
    ImageResource northEastEmphasis();

    @Source("gfx/nw_emph.png")
    ImageResource northWestEmphasis();

    @Source("gfx/sw_emph.png")
    ImageResource southWestEmphasis();

    @Source("gfx/se_emph.png")
    ImageResource southEastEmphasis();

    @Source("gfx/n_emph.png")
    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource northEmphasis();

    @Source("gfx/e_emph.png")
    @ImageOptions(repeatStyle=RepeatStyle.Vertical)
    ImageResource eastEmphasis();

    @Source("gfx/s_emph.png")
    @ImageOptions(repeatStyle=RepeatStyle.Horizontal)
    ImageResource southEmphasis();

    @Source("gfx/w_emph.png")
    @ImageOptions(repeatStyle=RepeatStyle.Vertical)
    ImageResource westEmphasis();


    public interface ICss extends CssResource {

      String frame();
      String container();
      String emphasis();

      String nw();
      String ne();
      String se();
      String sw();
      String n();
      String e();
      String s();
      String w();

    }

    @Source("FramePanel.css")
    ICss css();

  }

  public static IResources RESOURCES = GWT.create(IResources.class);

  public static IResources getResources() {
    return RESOURCES;
  }

  public static IResources.ICss getCss() {
    return getResources().css();
  }

  @UiField FlowPanel container;
  @UiField DivElement frame;

  public FramePanel() {
    getCss().ensureInjected();
    this.initWidget(BINDER.createAndBindUi(this));
  }

  private class HoverHandler implements MouseOverHandler, MouseOutHandler {
    @Override
    public void onMouseOut(MouseOutEvent event) {
      frame.removeClassName(getResources().css().emphasis());
    }
    @Override
    public void onMouseOver(MouseOverEvent event) {
      frame.addClassName(getResources().css().emphasis());
    }
  }

  public void setEmphasisOnHover(boolean value) {
    if (value) {
      HoverHandler handler = new HoverHandler();
      this.addDomHandler(handler, MouseOverEvent.getType());
      this.addDomHandler(handler, MouseOutEvent.getType());
    }
  }

  @Override
  public void add(Widget w) {
    container.add(w);
  }

  @Override
  public void clear() {
    container.clear();
  }

  @Override
  public Iterator<Widget> iterator() {
    return container.iterator();
  }

  @Override
  public boolean remove(Widget w) {
    return container.remove(w);
  }

}
