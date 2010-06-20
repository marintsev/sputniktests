package com.google.luna.client.utils;

import java.util.Iterator;
/**
 * The simplest possible bit vector backed by an array of booleans.
 * The vector has a fixed size and accessing elements outside the range
 * is an error.
 */
public class FlatBitVector implements IBitVector {

  public interface IListener {
    public void onBitChanged(int index, boolean value);
  }

  private final Listeners<IListener> listeners = new Listeners<IListener>();
  private final boolean[] bits;

  public FlatBitVector(int size) {
    bits = new boolean[size];
  }

  public FlatBitVector(int size, Iterable<Integer> bits) {
    this(size);
    for (int bit : bits) {
      assert bit < size;
      this.bits[bit] = true;
    }
  }

  public void addListener(IListener listener) {
    listeners.add(listener);
  }

  public void removeListener(IListener listener) {
    listeners.remove(listener);
  }

  @Override
  public boolean get(int index) {
    return bits[index];
  }

  @Override
  public void set(int index, boolean value) {
    if (bits[index] != value) {
      bits[index] = value;
      for (IListener listener : listeners)
        listener.onBitChanged(index, value);
    }
  }

  private class FlatBitSetIterator implements Iterator<Integer> {

    int next = -1;

    public FlatBitSetIterator() {
      advance();
    }

    @Override
    public Integer next() {
      int result = next;
      advance();
      return result;
    }

    @Override
    public boolean hasNext() {
      return next < bits.length;
    }

    private void advance() {
      if (!hasNext())
        throw new IllegalStateException();
      next++;
      while (next < bits.length && !bits[next])
        next++;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  public Iterator<Integer> iterator() {
    return new FlatBitSetIterator();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

}
