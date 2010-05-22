package com.google.luna.client.utils;


public interface IBitSet extends Iterable<Integer> {

  public interface IBitIterator {
    public int getNext();
    public boolean hasMore();
  }

  public void set(int index, boolean value);
  public boolean get(int index);

}
