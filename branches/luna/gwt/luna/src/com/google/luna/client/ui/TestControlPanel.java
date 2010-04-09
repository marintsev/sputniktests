// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TestControlPanel extends Composite implements ITestControlPanel {

  interface IMyUiBinder extends UiBinder<Widget, TestControlPanel> {}
  private static IMyUiBinder BINDER = GWT.create(IMyUiBinder.class);

  private final ArrayList<IHandler> handlers = new ArrayList<IHandler>();

  @UiField ProgressBar progress;
  @UiField Button reset;
  @UiField Button start;
  @UiField Label current;
  private IRunView.Mode currentMode = IRunView.Mode.DISABLED;

	public TestControlPanel() {
		this.initWidget(BINDER.createAndBindUi(this));
		start.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				switch (currentMode) {
				case RUNNING:
					firePauseClicked();
					break;
				case PAUSED:
					fireResumeClicked();
					break;
				default:
					fireStartClicked();
				  break;
				}
			}
		});
		reset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				fireResetClicked();
			}
		});
	}

	@Override
	public void updateStats(String testName) {
		current.setText(testName);
	}

	@Override
	public void setRunProgress(double value) {
	  progress.setRunProgress(value);
	}

	@Override
	public void setLoadProgress(double value) {
	  progress.setLoadProgress(value);
	}

	@Override
	public void addHandler(IHandler handler) {
		handlers.add(handler);
	}

	@Override
	public void removeHandler(IHandler handler) {
		handlers.remove(handler);
	}

	private void fireStartClicked() {
		for (IHandler handler : handlers)
			handler.startClicked();
	}

	private void firePauseClicked() {
		for (IHandler handler : handlers)
			handler.pauseClicked();
	}

	private void fireResumeClicked() {
		for (IHandler handler : handlers)
			handler.resumeClicked();
	}

	private void fireResetClicked() {
		for (IHandler handler : handlers)
			handler.resetClicked();
	}

	public void setMode(IRunView.Mode mode) {
		this.currentMode = mode;
		switch (mode) {
		case DISABLED:
			reset.setText("Reset");
			reset.setEnabled(false);
			start.setText("Start");
			start.setEnabled(false);
			break;
		case READY:
			reset.setText("Reset");
			reset.setEnabled(false);
			start.setText("Start");
			start.setEnabled(true);
			break;
		case RUNNING:
			reset.setText("Reset");
			reset.setEnabled(false);
			start.setText("Pause");
			start.setEnabled(true);
			break;
		case PAUSED:
			reset.setText("Reset");
			reset.setEnabled(true);
			start.setText("Resume");
			start.setEnabled(true);
			break;
		}
	}

}
