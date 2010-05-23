package com.google.luna.client.utils;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * A bit set consisting of a number of subsets each of a fixed size,
 * 'segmentSize'.  The segments are created on demand through the
 * {@link #newSegment(int, int)} method, which subclasses can override
 * to provide special behavior.
 *
 * A segment bit vector is infinitely large with all bits not explicitly
 * set to true being false.  Only true bits are represented.
 */
public class SegmentBitVector implements IBitVector {

  private final ArrayList<IBitVector> subsets = new ArrayList<IBitVector>();
  private final int segmentSize;

  public SegmentBitVector(int segmentSize) {
    this.segmentSize = segmentSize;
  }

  @Override
  public boolean get(int index) {
    int subsetIndex = index / segmentSize;
    IBitVector subset = subsets.size() <= subsetIndex ? null : subsets.get(subsetIndex);
    if (subset == null)
      return false;
    return subset.get(index % segmentSize);
  }

  protected void ensureSegmentsPresent(int count) {
    while (subsets.size() < count)
      subsets.add(newSegment(subsets.size(), segmentSize));
  }

  @Override
  public void set(int index, boolean value) {
    int subsetIndex = index / segmentSize;
    IBitVector subset = subsets.size() <= subsetIndex ? null : subsets.get(subsetIndex);
    if (subset == null) {
      subset = newSegment(subsetIndex, segmentSize);
      // How stupid is this?
      while (subsets.size() <= subsetIndex)
        subsets.add(null);
      subsets.set(subsetIndex, subset);
    }
    subset.set(index % segmentSize, value);
  }

  /**
   * Create a new bit vector that can hold at least size entries to be
   * used as the index'th segment.  The index'th segment holds the bits
   * from (segmentSize * index) to (segmentSize * (index + 1)) - 1.
   *
   * The default behavior is to return a flat bit vector.  Override to
   * provide specialized behavior.
   */
  protected IBitVector newSegment(int index, int size) {
    return new FlatBitVector(size);
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
        IBitVector subset = subsets.get(subsetIndex);
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
    return new SegmentBitSetIterator();
  }

}
