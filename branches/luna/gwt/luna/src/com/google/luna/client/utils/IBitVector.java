package com.google.luna.client.utils;

/**
 * A linear sequence of bits.
 */
public interface IBitVector extends Iterable<Integer> {

  public void set(int index, boolean value);
  public boolean get(int index);

}
