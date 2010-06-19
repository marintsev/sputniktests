package com.google.luna.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.luna.client.LunaTestCase;
import com.google.luna.client.utils.Cookie.Factory;

public class PersistentEnumVectorTest extends LunaTestCase {

  private enum Suit {
    HEARTS, CLUBS, DIAMONDS, SPADES
  }

  @Test
  public void testVector() {
    TestCookieJar jar = new TestCookieJar();
    Factory factory = new Factory(jar);
    PersistentEnumVector<Suit> vector = new PersistentEnumVector<Suit>(
        Suit.values(), 1024, 50, factory);
    PseudoRandom pr = new PseudoRandom(-37);
    List<Suit> reference = new ArrayList<Suit>();
    for (int i = 0; i < 1024; i++) {
      Suit value = pr.nextElement(Suit.values());
      vector.set(i, value);
      reference.add(value);
    }
    for (int i = 0; i < 1024; i++)
      assertEquals(reference.get(i), vector.get(i));
    PersistentEnumVector<Suit> newVector = new PersistentEnumVector<Suit>(
        Suit.values(), 1024, 50, factory);
    for (int i = 0; i < 1024; i++)
      assertEquals(reference.get(i), newVector.get(i));
  }

}
