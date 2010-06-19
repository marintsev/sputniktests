// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.test.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.luna.client.Constants;
import com.google.luna.client.Luna;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.rmi.BlockList;
import com.google.luna.client.rmi.Backend.Case;
import com.google.luna.client.rmi.Backend.CaseBlock;
import com.google.luna.client.utils.Listeners;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

class TestSuite {

  /**
   * Listener for test suite related load events.
   */
  public interface IListener {

    /**
     * Called when this test suite has fetched a new test block.  The
     * range of serial numbers within the test suite is given as parameters.
     */
    public void onTestBlockLoaded(int from, int to);

  }

  private final Backend.Suite data;
  private final ITestCase.IFactory factory;
  private final BlockList<ITestCase> cases;
  private final Listeners<IListener> listeners = new Listeners<IListener>();

  public TestSuite(final Backend.Suite data, ITestCase.IFactory factory,
      final int serialOffset) {
    this.data = data;
    this.factory = factory;
    this.cases = new BlockList<ITestCase>(this.getCaseCount(), Constants.kTestBlockSize,
        Constants.kTestBlockAheadCount) {
      @Override
      protected Promise<List<ITestCase>> fetchBlock(int from, int to) {
        return fetchCaseBlock(from, to, serialOffset);
      }
    };
    this.cases.addListener(new BlockList.IListener() {
      @Override
      public void onBlockLoaded(int from, int to) {
        fireTestBlockLoaded(from, to);
      }
    });
  }

  private void fireTestBlockLoaded(int from, int to) {
    for (IListener listener : listeners)
      listener.onTestBlockLoaded(from, to);
  }

  public void addListener(IListener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(IListener listener) {
    this.listeners.remove(listener);
  }

  protected Promise<List<ITestCase>> fetchCaseBlock(final int from,
      final int to, final int serialOffset) {
    final Promise<List<ITestCase>> result = new Promise<List<ITestCase>>();
    Luna.getBackend().getCases(data, from, to).onValue(new Thunk<Backend.CaseBlock>() {
      @Override
      public void onValue(CaseBlock block) {
        assert block.getStart() == from;
        assert block.getEnd() == to;
        JsArray<Case> cases = block.getCases();
        ArrayList<ITestCase> list = new ArrayList<ITestCase>();
        for (int i = 0; i < cases.length(); i++) {
          int serial = serialOffset + from + i;
          list.add(factory.create(cases.get(i), serial));
        }
        result.setValue(list);
      }
    });
    return result;
  }

  public int getCaseCount() {
    return data.getCaseCount();
  }

  public Promise<ITestCase> getCase(int index) {
    return cases.get(index);
  }

}
