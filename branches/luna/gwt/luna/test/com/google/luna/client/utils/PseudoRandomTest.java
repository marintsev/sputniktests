package com.google.luna.client.utils;

import org.junit.Test;

import com.google.luna.LunaTestCase;

public class PseudoRandomTest extends LunaTestCase {

  @Test
  public void testRandomGeneration() {
    PseudoRandom pr = new PseudoRandom(27, 39);
    for (int i = 1; i < 10000; i++) {
      int next = pr.nextInt(0, i);
      assertTrue(next >= 0);
      assertTrue(next < i);
    }
  }

}
