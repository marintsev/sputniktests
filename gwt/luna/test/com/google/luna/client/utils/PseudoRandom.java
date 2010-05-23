package com.google.luna.client.utils;


public class PseudoRandom {

  private int i, j;

  public PseudoRandom(int seedI, int seedJ) {
    this.i = seedI;
    this.j = seedJ;
  }

  public PseudoRandom(int seed) {
    this(seed - 4, seed + 4);
  }

  public PseudoRandom() {
    this(0);
  }

  public int nextInt(int low, int high) {
    int k = ((j * 237) + 37) & 0xFFFFFFFF;
    j = ((i + -83) + 61) & 0xFFFFFFFF;
    i = k;
    int random = k;
    // If the random value is negative we don't just invert it since
    // that would make 0 only half as likely.  Rather, we shift the
    // value one up and then invert it.
    if (random < 0) random = -(random + 1);
    return (random % (high - low)) + low;
  }

  public boolean nextBoolean() {
    return nextInt(0, 2) == 0;
  }

  public <T> T nextElement(T[] elms) {
    int index = nextInt(0, elms.length);
    return elms[index];
  }

}
