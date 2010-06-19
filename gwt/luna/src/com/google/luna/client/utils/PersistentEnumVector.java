package com.google.luna.client.utils;

import com.google.luna.client.utils.Cookie.Factory;
/**
 * An enum vector that stores its values in cookies generated from the
 * given cookie factory.
 */
public class PersistentEnumVector<T extends Enum<T>> extends EnumVector<T> {

  private final int segmentSize;
  private final Factory factory;
  private boolean allowInit = false;

  public PersistentEnumVector(T[] enums, int size, int segmentSize,
      Factory domain) {
    super(enums, size);
    allowInit = true;
    this.segmentSize = segmentSize;
    this.factory = domain;
    this.initialize();
  }

  @Override
  protected void initialize() {
    if (allowInit)
      super.initialize();
  }

  @Override
  protected IBitVector newBitVector(T value, int size) {
    assert value != null;
    Factory subFactory = factory.child(value.name());
    return new PersistentBitVector(segmentSize, subFactory);
  }

}
