// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.ui;
public interface ITestControlPanel {

	public interface IHandler {
		public void resetClicked();
		public void startClicked();
		public void pauseClicked();
		public void resumeClicked();
	}

	public void addHandler(IHandler handler);
	public void removeHandler(IHandler handler);

	public void setRunProgress(double value);
	public void setLoadProgress(double value);
	public void updateStats(String testName, int totalCount,
			int succeededCount, int failedCount);

}
