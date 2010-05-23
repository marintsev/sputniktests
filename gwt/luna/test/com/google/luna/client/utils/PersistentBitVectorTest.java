package com.google.luna.client.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.luna.LunaTestCase;
import com.google.luna.client.utils.Cookie.Factory;

public class PersistentBitVectorTest extends LunaTestCase {

  @Test
  public void testPersistentBitVector() {
    TestCookieJar testJar = new TestCookieJar();
    Factory factory = new Factory(testJar);
    SegmentBitVector bits = new PersistentBitVector(50, factory);
    Set<Integer> reference = new HashSet<Integer>();
    PseudoRandom pr = new PseudoRandom(49, 35);
    for (int i = 0; i < 100; i++) {
      int value = pr.nextInt(0, 1000);
      bits.set(value, true);
      reference.add(value);
    }
    for (int i = 0; i < 1000; i++)
      assertEquals(reference.contains(i), bits.get(i));

    SegmentBitVector newBits = new PersistentBitVector(50, factory);
    for (int i = 0; i < 1000; i++)
      assertEquals(reference.contains(i), newBits.get(i));
  }

}
