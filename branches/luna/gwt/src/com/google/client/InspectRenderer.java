// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.
package com.google.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.client.rmi.Backend;
import com.google.client.utils.Promise;
import com.google.client.utils.Thunk;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class InspectRenderer extends MainPageRenderer {

  private static interface LazyTreeItem {
    public Promise<String> getLabel();
    public Promise<List<LazyTreeItem>> getChildren();
  }

  private static class LazyTree extends Tree {

    private final Sputnik sputnik;
    private final Map<LazyTreeItem, Promise<List<LazyTreeItem>>> childCache = new HashMap<LazyTreeItem, Promise<List<LazyTreeItem>>>();

    private Promise<List<LazyTreeItem>> getChildren(LazyTreeItem item) {
      Promise<List<LazyTreeItem>> result = childCache.get(item);
      if (result == null) {
        result = item.getChildren();
        childCache.put(item, result);
      }
      return result;
    }


    private TreeItem adaptItem(LazyTreeItem item) {
      final TreeItem result = new TreeItem();
      item.getLabel().onValue(sputnik.new SimpleThunk<String>() {
        @Override
        public void onValue(String label) {
          result.setText(label);
        }
      });
      return result;
    }

    public LazyTree(Sputnik sputnik, LazyTreeItem root) {
      this.sputnik = sputnik;
      getChildren(root).onValue(sputnik.new SimpleThunk<List<LazyTreeItem>>() {
        @Override
        public void onValue(List<LazyTreeItem> items) {
          for (LazyTreeItem item : items)
            addItem(adaptItem(item));
        }
      });
    }

  }

  private class RootItem implements LazyTreeItem {

    @Override
    public Promise<String> getLabel() {
      return null;
    }

    @Override
    public Promise<List<LazyTreeItem>> getChildren() {
      final Promise<List<LazyTreeItem>> result = new Promise<List<LazyTreeItem>>();
      getBackend().getSuites().onValue(new Thunk<JsArray<Backend.Suite>>() {
        @Override
        public void onValue(JsArray<Backend.Suite> suites) {
          List<LazyTreeItem> list = new ArrayList<LazyTreeItem>();
          for (int i = 0; i < suites.length(); i++)
            list.add(new SuiteItem(suites.get(i)));
          result.setValue(list);
        }
        @Override
        public void onError(String message) {
          result.setError(message);
        }
      });
      return result;
    }

  }

  private class SuiteItem implements LazyTreeItem {

    private final Backend.Suite suite;

    public SuiteItem(Backend.Suite suite) {
      this.suite = suite;
    }

    @Override
    public Promise<String> getLabel() {
      String name = "{ family: " + suite.getFamily() + ", name: " + suite.getName() +
        ", size: " + suite.getSize() + " key: " + suite.getKey().asString() + "}";
      return Promise.from(name);
    }

    @Override
    public Promise<List<LazyTreeItem>> getChildren() {
      return null;
    }

  }

  @Override
  public void renderContents(Panel root) {
    LazyTree tree = new LazyTree(getSputnik(), new RootItem());
    root.add(tree);
  }

}
