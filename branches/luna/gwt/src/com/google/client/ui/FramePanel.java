package com.google.client.ui;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class FramePanel extends Composite implements HasWidgets {

	interface FramePanelUiBinder extends UiBinder<Widget, FramePanel> {}
  private static FramePanelUiBinder BINDER = GWT.create(FramePanelUiBinder.class);

	public interface Resources extends ClientBundle {

		@Source("gfx/ne.png") ImageResource northEast();
		@Source("gfx/nw.png") ImageResource northWest();
		@Source("gfx/sw.png") ImageResource southWest();
		@Source("gfx/se.png") ImageResource southEast();

		@Source("gfx/ne_sel.png") ImageResource northEastSelected();
		@Source("gfx/nw_sel.png") ImageResource northWestSelected();
		@Source("gfx/sw_sel.png") ImageResource southWestSelected();
		@Source("gfx/se_sel.png") ImageResource southEastSelected();

		public interface Css extends CssResource {

			String frame();
			String border();
			String container();

			String nw();
			String ne();
			String se();
			String sw();

		}

		@Source("FramePanel.css")
		Css css();

	}

	public static Resources RESOURCES = GWT.create(Resources.class);

	public static Resources getResources() {
		return RESOURCES;
	}

	public static Resources.Css getCss() {
		return getResources().css();
	}

	@UiField FlowPanel container;

	public FramePanel() {
		getCss().ensureInjected();
		this.initWidget(BINDER.createAndBindUi(this));
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
