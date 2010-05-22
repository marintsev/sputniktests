package com.google.luna.client.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class SegmentBitSet implements IBitSet {

  public interface IListener {
    public void onSubsetCreated(int index, FlatBitSet subset);
  }

  private final Listeners<IListener> listeners = new Listeners<IListener>();
  private final ArrayList<FlatBitSet> subsets = new ArrayList<FlatBitSet>();
  private final int segmentSize;

  public SegmentBitSet(int segmentSize) {
    this.segmentSize = segmentSize;
  }

  @Override
  public boolean get(int index) {
    int subsetIndex = index / segmentSize;
    FlatBitSet subset = subsets.size() <= subsetIndex ? null : subsets.get(subsetIndex);
    if (subset == null)
      return false;
    return subset.get(index % segmentSize);
  }

  @Override
  public void set(int index, boolean value) {
    int subsetIndex = index / segmentSize;
    FlatBitSet subset = subsets.size() <= subsetIndex ? null : subsets.get(subsetIndex);
    if (subset == null) {
      subset = new FlatBitSet(segmentSize);
      // How stupid is this?
      while (subsets.size() <= subsetIndex)
        subsets.add(null);
      subsets.set(subsetIndex, subset);
      for (IListener listener : listeners)
        listener.onSubsetCreated(subsetIndex, subset);
    }
    subset.set(index % segmentSize, value);
  }

  private class SegmentBitSetIterator implements Iterator<Integer> {

    private Iterator<Integer> currentSubIterator;
    private int subsetIndex = -1;

    SegmentBitSetIterator() {
      advance();
    }

    @Override
    public Integer next() {
      if (!hasNext())
        throw new IllegalStateException();
      int result = currentSubIterator.next();
      int offset = subsetIndex * segmentSize;
      if (!currentSubIterator.hasNext())
        advance();
      return offset + result;
    }

    @Override
    public boolean hasNext() {
      return currentSubIterator != null;
    }

    private void advance() {
      subsetIndex++;
      while (subsetIndex < subsets.size()) {
        FlatBitSet subset = subsets.get(subsetIndex);
        if (subset != null) {
          currentSubIterator = subset.iterator();
          if (currentSubIterator.hasNext())
            return;
        }
        subsetIndex++;
      }
      currentSubIterator = null;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  @Override
  public Iterator<Integer> iterator() {
    // TODO Auto-generated method stub
    return new SegmentBitSetIterator();
  }

}
