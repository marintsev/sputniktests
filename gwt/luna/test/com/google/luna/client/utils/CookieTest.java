package com.google.luna.client.utils;

import com.google.luna.client.LunaTestCase;
import com.google.luna.client.utils.Cookie.Factory;

public class CookieTest extends LunaTestCase {

  public void testIntsSimple() {
    Factory domain = new Factory(new FakeCookieJar());

    Cookie<Integer> cookie = domain.integer("foo");
    assertFalse(cookie.exists());
    assertEquals(0, (int) cookie.get());
    cookie.set(18);
    assertTrue(cookie.exists());
    assertEquals(18, (int) cookie.get());

    Cookie<Integer> newCookie = domain.integer("foo");
    assertTrue(newCookie.exists());
    assertEquals(18, (int) newCookie.get());
  }

}
