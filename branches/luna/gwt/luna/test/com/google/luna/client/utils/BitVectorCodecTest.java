package com.google.luna.client.utils;

import com.google.luna.client.LunaTestCase;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class BitVectorCodecTest extends LunaTestCase {

  @Test
  public void testCodec() {
    final int kSize = 512;
    for (int i = 1800; i < 1900; i++) {
      PseudoRandom pr = new PseudoRandom(i);
      FlatBitVector vector = new FlatBitVector(kSize);
      Set<Integer> reference = new HashSet<Integer>();
      int count = pr.nextInt(0, kSize);
      for (int k = 0; k < count; k++) {
        int value = pr.nextInt(0, kSize);
        vector.set(value, true);
        reference.add(value);
      }
      String coded = BitVectorCodec.encode(vector);
      FlatBitVector decoded = new FlatBitVector(kSize, BitVectorCodec.decode(coded));
      for (int k = 0; k < kSize; k++)
        assertEquals(reference.contains(k), decoded.get(k));
    }
  }

  @Test
  public void testSparse() {
    final int kSize = 1024;
    for (int i = 2100; i < 2200; i++) {
      PseudoRandom pr = new PseudoRandom(i);
      FlatBitVector vector = new FlatBitVector(kSize);
      Set<Integer> reference = new HashSet<Integer>();
      for (int k = 0; k < kSize / 16; k++) {
        int value = pr.nextInt(0, kSize);
        vector.set(value, true);
        reference.add(value);
      }
      String coded = BitVectorCodec.encode(vector);
      FlatBitVector decoded = new FlatBitVector(kSize, BitVectorCodec.decode(coded));
      for (int k = 0; k < kSize; k++)
        assertEquals(reference.contains(k), decoded.get(k));
    }
  }

}
