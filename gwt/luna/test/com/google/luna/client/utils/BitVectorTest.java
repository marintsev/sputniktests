package com.google.luna.client.utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.luna.LunaTestCase;

public class BitVectorTest extends LunaTestCase {

  @Test
  public void testFlatBitSet() {
    runSettingAndGettingTest(new FlatBitVector(1024), 1024);
  }

  @Test
  public void testSegmentedBitSets() {
    for (int i = 1; i < 2048; i += Math.max((int) (i * 0.2), 1))
      runSettingAndGettingTest(new SegmentBitVector(i), 1024);
  }

  public void runSettingAndGettingTest(IBitVector set, int size) {
    Set<Integer> nums = new HashSet<Integer>();
    for (int i = 0; i < size; i += Math.max((int) (i * 0.1), 1)) {
      set.set(i, true);
      nums.add(i);
    }
    for (int i = 0; i < size; i++)
      assertEquals(nums.contains(i), set.get(i));
    int last = -1;
    for (int i : set) {
      assertTrue(last < i);
      assertTrue(nums.contains(i));
      nums.remove(i);
      last = i;
    }
    assertTrue(nums.isEmpty());
  }

  /**
   * Test that flat bit sets only invoke their listeners on genuine
   * bit changes.
   */
  @Test
  public void testFlatBitSetListeners() {
    PseudoRandom pr = new PseudoRandom();
    final boolean[] reference = new boolean[1024];
    FlatBitVector bits = new FlatBitVector(1024);
    final int[] expectedIndex = {0};
    final boolean[] expectedValue = {false};
    final boolean[] hadCallback = {false};
    bits.addListener(new FlatBitVector.IListener() {
      @Override
      public void onBitChanged(int index, boolean value) {
        assertEquals(expectedIndex[0], index);
        assertEquals(expectedValue[0], value);
        hadCallback[0] = true;
      }
    });
    for (int i = 0; i < 1000; i++) {
      int index = pr.nextInt(0, 1024);
      boolean doChange = pr.nextBoolean();
      boolean value = doChange ? !reference[index] : reference[index];
      expectedIndex[0] = index;
      expectedValue[0] = value;
      hadCallback[0] = false;
      reference[index] = value;
      bits.set(index, value);
      assertEquals(doChange, hadCallback[0]);
    }
  }

}
