package com.google.luna.client.utils;

import org.junit.Test;

import com.google.luna.client.LunaTestCase;


public class EnumVectorTest extends LunaTestCase {

  private enum Color { RED, GREEN, BLUE };

  @Test
  public void testRandom() {
    EnumVector<Color> set = new EnumVector<Color>(Color.values(), 1024);
    Color[] colors = new Color[1024];
    PseudoRandom pr = new PseudoRandom(31);
    for (int i = 0; i < 1024; i++) {
      assertEquals(Color.RED, set.get(i));
      Color color = pr.nextElement(Color.values());
      set.set(i, color);
      colors[i] = color;
    }
    for (int i = 0; i < 1024; i++)
      assertEquals(colors[i], set.get(i));
    for (int i = 0; i < 1024; i++) {
      Color newColor = pr.nextElement(Color.values());
      colors[i] = newColor;
      set.set(i, newColor);
    }
    for (int i = 0; i < 1024; i++)
      assertEquals(colors[i], set.get(i));
  }

  public class EnumSelector<T extends Enum<T>> {

    private int index = -1;
    private final Class<T> klass;

    public EnumSelector(Class<T> klass) {
      this.klass = klass;
    }

    public T next() {
      T[] enums = klass.getEnumConstants();
      index = (index + 1) % enums.length;
      return enums[index];
    }

  }

  public void testEnumConstants() {
    EnumSelector<Color> es = new EnumSelector<Color>(Color.class);
    for (int i = 0; i < 100; i++) {
      assertEquals(Color.values()[i % 3], es.next());
    }
  }

}
