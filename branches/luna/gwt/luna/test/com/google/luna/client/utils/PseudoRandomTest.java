package com.google.luna.client.utils;

import com.google.luna.client.LunaTestCase;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PseudoRandomTest extends LunaTestCase {

  @Test
  public void testRandomGeneration() {
    PseudoRandom pr = new PseudoRandom(27);
    for (int i = 1; i < 10000; i++) {
      int next = pr.nextInt(0, i);
      assertTrue(next >= 0);
      assertTrue(next < i);
    }
  }

  @Test
  public void testShuffle() {
    PseudoRandom pr = new PseudoRandom(88);
    Set<Integer> ints = new HashSet<Integer>();
    for (int i : pr.getRandomSequence(100, 200)) {
      assertTrue(i >= 100);
      assertTrue(i < 200);
      assertTrue(!ints.contains(i));
      ints.add(i);
    }
    assertEquals(100, ints.size());
  }

  @Test
  public void testDistribution() {
    // This is not as much a test as a canary.  If this starts failing
    // you need to consider whether the sequence generator is still
    // working.  If it is it will be easy to change the parameters to
    // make this test pass again.
    int[][] distribution = new int[32][32];
    for (int i = 0; i < 32; i++) {
      for (int j = 0; j < 32; j++) {
        PseudoRandom pr = new PseudoRandom(11 * i + 13 * j + 3);
        int pos = 0;
        for (int v : pr.getRandomSequence(0, 32)) {
          distribution[pos][v]++;
          pos++;
        }
      }
    }
    int smallest = Integer.MAX_VALUE;
    int largest = Integer.MIN_VALUE;
    for (int i = 0; i < 32; i++) {
      for (int j = 0; j < 32; j++) {
        int value = distribution[i][j];
        if (value < smallest) smallest = value;
        if (value > largest) largest = value;
      }
    }
    assertTrue(smallest > 0);
    assertTrue(largest < 64);
  }

}
