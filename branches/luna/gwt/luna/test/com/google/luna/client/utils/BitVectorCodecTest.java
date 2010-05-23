package com.google.luna.client.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.luna.LunaTestCase;

public class BitVectorCodecTest extends LunaTestCase {

  @Test
  public void testCodec() {
    for (int i = 80; i < 90; i += 1) {
      for (int j = 150; j < 160; j += 1) {
        PseudoRandom pr = new PseudoRandom(i, j);
        FlatBitVector vector = new FlatBitVector(512);
        Set<Integer> reference = new HashSet<Integer>();
        for (int k = 0; k < 100; k += 5) {
          int value = pr.nextInt(0, 512);
          vector.set(value, true);
          reference.add(value);
        }
        String coded = BitVectorCodec.encode(vector);
        FlatBitVector decoded = new FlatBitVector(512, BitVectorCodec.decode(coded));
        for (int k = 0; k < 512; k++)
          assertEquals(reference.contains(k), decoded.get(k));
      }
    }
  }

}
