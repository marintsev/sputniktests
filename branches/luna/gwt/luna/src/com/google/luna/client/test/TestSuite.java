// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.luna.client.Luna;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.rmi.BlockList;
import com.google.luna.client.rmi.Backend.Case;
import com.google.luna.client.rmi.Backend.CaseBlock;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class TestSuite {

	public interface ILoadListener {
		public void hasLoaded(TestSuite suite, int max);
	}

	private final int BLOCK_SIZE = 128;
	private final int BLOCK_AHEAD_COUNT = 3;

	private final Backend.Suite data;
	private final ITestController controller;
	private final BlockList<TestCase> cases;
	private final ArrayList<ILoadListener> listeners = new ArrayList<ILoadListener>();

	public TestSuite(final Backend.Suite data, ITestController controller) {
	  this.data = data;
	  this.controller = controller;
	  this.cases = new BlockList<TestCase>(this.getCaseCount(), BLOCK_SIZE, BLOCK_AHEAD_COUNT) {
	  	@Override
	  	protected Promise<List<TestCase>> fetchBlock(int from, int to) {
	  		return fetchCaseBlock(from, to);
	  	}
	  };
	  this.cases.addListener(new BlockList.IListener() {
	  	@Override
	  	public void hasLoaded(int max) {
	  		fireCasesLoaded(max);
	  	}
	  });
	}

	private void fireCasesLoaded(int max) {
		for (ILoadListener listener : listeners) {
			listener.hasLoaded(this, max);
		}
	}

	public void addLoadListener(ILoadListener listener) {
		this.listeners.add(listener);
	}

	public void removeLoadListener(ILoadListener listener) {
		this.listeners.remove(listener);
	}

	protected Promise<List<TestCase>> fetchCaseBlock(final int from,
			final int to) {
		final Promise<List<TestCase>> result = new Promise<List<TestCase>>();
		Luna.getBackend().getCases(data, from, to).onValue(new Thunk<Backend.CaseBlock>() {
			@Override
			public void onValue(CaseBlock block) {
				assert block.getStart() == from;
				assert block.getEnd() == to;
				JsArray<Case> cases = block.getCases();
				ArrayList<TestCase> list = new ArrayList<TestCase>();
				for (int i = 0; i < cases.length(); i++) {
					int serial = from + i;
					list.add(new TestCase(cases.get(i), serial, controller));
				}
				result.setValue(list);
			}
		});
		return result;
	}

	public int getCaseCount() {
		return data.getCaseCount();
	}

	public Promise<TestCase> getCase(int index) {
		return cases.get(index);
	}

}
