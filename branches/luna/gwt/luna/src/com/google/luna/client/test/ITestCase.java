package com.google.luna.client.test;

import com.google.luna.client.rmi.Backend.Case;
import com.google.luna.client.utils.Promise;

public interface ITestCase {

	public interface IFactory {
		public ITestCase create(Case data, int serial);
	}

	public int getSerial();

	public Promise<String> getDescription();

	public Promise<String> getLabel();

	public boolean isNegative();

	public void schedule(ITestProgressSink progress);

}
