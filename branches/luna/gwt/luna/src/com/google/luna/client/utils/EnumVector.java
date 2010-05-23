package com.google.luna.client.utils;
/**
 * A vector of enum values backed by as mant bit vectors as there are
 * enum values minus 1.  The default value for all entries is the first
 * enum value, the one with ordinal 0, and this value takes no space to
 * represent.  This representation cannot store null.
 */
public class EnumVector<T extends Enum<T>> {

  private final T[] enums;
  private final IBitVector[] bitSets;
  private final int size;

  public EnumVector(T[] enums, int size) {
    this.enums = enums;
    this.size = size;
    this.bitSets = new IBitVector[enums.length - 1];
    initialize();
  }

  protected void initialize() {
    for (int i = 0; i < bitSets.length; i++)
      bitSets[i] = newBitVector(enums[i + 1], size);
  }

  public void set(int index, T value) {
    int ord = value.ordinal();
    for (int i = 0; i < bitSets.length; i++)
      bitSets[i].set(index, (i + 1) == ord);
  }

  public T get(int index) {
    for (int i = 0; i < bitSets.length; i++) {
      if (bitSets[i].get(index))
        return enums[i + 1];
    }
    return enums[0];
  }

  protected IBitVector newBitVector(T value, int size) {
    return new FlatBitVector(size);
  }

}
