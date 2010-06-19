package com.google.luna.client.utils;

import com.google.luna.client.LunaTestCase;
import com.google.luna.client.utils.Cookie.Factory;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PersistentBitVectorTest extends LunaTestCase {

  @Test
  public void testPersistentBitVector() {
    FakeCookieJar testJar = new FakeCookieJar();
    Factory factory = new Factory(testJar);
    SegmentBitVector bits = new PersistentBitVector(50, factory);
    Set<Integer> reference = new HashSet<Integer>();
    PseudoRandom pr = new PseudoRandom(49);
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

    newBits.clear();
    for (int i = 0; i < 1000; i++)
      assertFalse(newBits.get(i));

    SegmentBitVector newestBits = new PersistentBitVector(50, factory);
    for (int i = 0; i < 1000; i++)
      assertFalse(newestBits.get(i));
  }

}
