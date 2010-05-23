package com.google.luna.client.utils;

import com.google.luna.client.utils.Cookie.Factory;
/**
 * A bit vector that persists its values in cookies created from a given
 * cookie factory.  Values are stored in a number of separate cookies to
 * keep the max size of the cookies involved down and to reduce the size
 * of cookie payload, so only a single segment has to be re-encoded and
 * stored when a bit changes.
 */
public class PersistentBitVector extends SegmentBitVector {

  private final Factory factory;
  private final Cookie<Integer> segmentCount;

  public PersistentBitVector(int segmentSize, Factory factory) {
    super(segmentSize);
    this.factory = factory;
    this.segmentCount = factory.integer("segments");
    this.ensureSegmentsPresent(segmentCount.get());
  }

  @Override
  protected IBitVector newSegment(int index, int size) {
    if (index >= segmentCount.get())
      segmentCount.set(index + 1);
    final Cookie<String> cookie = factory.string(Integer.toString(index));
    Iterable<Integer> stored = cookie.exists()
        ? BitVectorCodec.decode(cookie.get())
        : null;
    final FlatBitVector result;
    if (stored != null) {
      result = new FlatBitVector(size, stored);
    } else {
      result = new FlatBitVector(size);
    }
    result.addListener(new FlatBitVector.IListener() {
      @Override
      public void onBitChanged(int index, boolean value) {
        cookie.set(BitVectorCodec.encode(result));
      }
    });
    return result;
  }

}
