package com.google.luna.client.utils;

import java.util.ArrayList;
import java.util.List;


public class PseudoRandom {

  private static final int kN = 624;
  private static final int kM = 397;
  private static final int kMatrixA = 0x9908b0df;
  private static final int kUpperMask = 0x80000000;
  private static final int kLowerMask = 0x7fffffff;
  private static final int kTemperingMaskB = 0x9d2c5680;
  private static final int kTemperingMaskC = 0xefc60000;

  private int mt[];
  private int mti;
  private int mag01[];

  public PseudoRandom(long seed) {
    mt = new int[kN];
    mag01 = new int[2];
    mag01[0] = 0x0;
    mag01[1] = kMatrixA;

    mt[0]= (int)(seed & 0xffffffff);
    for (mti=1; mti<kN; mti++) {
      mt[mti] =  (1812433253 * (mt[mti-1] ^ (mt[mti-1] >>> 30)) + mti);
      mt[mti] &= 0xffffffff;
    }
  }

  public PseudoRandom() {
    this(4357);
  }

  public double nextDouble() {
    return (((long) next(26) << 27) + next(27)) / (double) (1L << 53);
  }

  private int next(final int bits) {
    int y;
    if (mti >= kN) {
      int kk;
      for (kk = 0; kk < kN - kM; kk++) {
        y = (this.mt[kk] & kUpperMask) | (this.mt[kk+1] & kLowerMask);
        this.mt[kk] = this.mt[kk+kM] ^ (y >>> 1) ^ this.mag01[y & 0x1];
      }
      for (; kk < kN-1; kk++) {
        y = (this.mt[kk] & kUpperMask) | (this.mt[kk+1] & kLowerMask);
        this.mt[kk] = this.mt[kk+(kM-kN)] ^ (y >>> 1) ^ this.mag01[y & 0x1];
      }
      y = (this.mt[kN-1] & kUpperMask) | (this.mt[0] & kLowerMask);
      this.mt[kN-1] = this.mt[kM-1] ^ (y >>> 1) ^ this.mag01[y & 0x1];
      mti = 0;
    }
    y = mt[mti++];
    y ^= y >>> 11;
    y ^= (y << 7) & kTemperingMaskB;
    y ^= (y << 15) & kTemperingMaskC;
    y ^= (y >>> 18);

    return y >>> (32 - bits);
  }

  public int nextInt(int from, int to) {
    int n = to - from;
    int value;
    if ((n & -n) == n) {
      value = (int) ((n * (long) next(31)) >> 31);
    } else {
      int bits;
      do {
        bits = next(31);
        value = bits % n;
      }
      while(bits - value + (n - 1) < 0);
    }
    return from + value;
  }

  public boolean nextBoolean() {
    return nextInt(0, 2) == 0;
  }

  public <T> T nextElement(T[] elms) {
    int index = nextInt(0, elms.length);
    return elms[index];
  }

  public Iterable<Integer> getRandomSequence(int from, int to) {
    List<Integer> result = new ArrayList<Integer>();
    // Populate result with sequential numbers.
    for (int i = from; i < to; i++)
      result.add(i);
    // Shuffle result.
    int count = to - from;
    for (int i = 0; i < count; i++) {
      // Pick a random position in [i .. count] and swap it with position
      // i.
      int pos = nextInt(i, count);
      int value = result.get(pos);
      result.set(pos, result.get(i));
      result.set(i, value);
    }
    return result;
  }

}
