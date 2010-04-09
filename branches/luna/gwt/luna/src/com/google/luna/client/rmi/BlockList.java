// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client.rmi;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public abstract class BlockList<T> {

	public interface IListener {
		public void hasLoaded(int max);
	}

	private final int size;
	private final Vector<Promise<List<T>>> blocks;
	private final Vector<Promise<T>> elements;
	private final List<IListener> listeners = new ArrayList<IListener>();
	private final int blockSize;
	private final int blockAheadCount;
	private int maxLoaded = 0;

	public BlockList(int size, int blockSize, int blockAheadCount) {
		this.size = size;
		this.blockSize = blockSize;
		this.blockAheadCount = blockAheadCount;
		this.blocks = new Vector<Promise<List<T>>>();
		this.blocks.setSize(size / blockSize + 1);
		this.elements = new Vector<Promise<T>>();
		this.elements.setSize(size);
	}

	public void addListener(IListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(IListener listener) {
		this.listeners.remove(listener);
	}

	public Promise<T> get(int index) {
		ensureIndex(index);
		return elements.get(index);
	}

	private void ensureIndex(final int index) {
		if (elements.get(index) != null)
			return;
		final int block = index / blockSize;
		int limit = Math.min(block + blockAheadCount + 1, blocks.size());
		for (int i = block; i < limit; i++)
			ensureBlock(i);
		final Promise<T> result = new Promise<T>();
		elements.set(index, result);
		blocks.get(block).onValue(new Thunk<List<T>>() {
			@Override
			public void onValue(List<T> values) {
				int start = block * blockSize;
				result.setValue(values.get(index - start));
			}
		});
	}

	private void ensureBlock(int block) {
		if (blocks.get(block) != null)
			return;
		final int min = block * blockSize;
		if (min >= size)
			return;
		final Promise<List<T>> result = new Promise<List<T>>();
		blocks.set(block, result);
		final int max = Math.min(min + blockSize, size);
		fetchBlock(min, max).onValue(new Thunk<List<T>>() {
			@Override
			public void onValue(List<T> values) {
				result.setValue(values);
				if (max > maxLoaded)
					newMaxLoaded(max);
			}
		});
	}

	private void newMaxLoaded(int value) {
		this.maxLoaded = value;
		for (IListener listener : listeners)
			listener.hasLoaded(value);
	}

	protected abstract Promise<List<T>> fetchBlock(int from, int to);

}
