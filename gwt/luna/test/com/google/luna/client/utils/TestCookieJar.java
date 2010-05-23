package com.google.luna.client.utils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestCookieJar implements ICookieJar {

  private final Map<String, String> cookies = new HashMap<String, String>();

  @Override
  public void clear(String key) {
    cookies.remove(key);
  }

  @Override
  public String get(String key) {
    return cookies.get(key);
  }

  @Override
  public boolean exists(String key) {
    return cookies.containsKey(key);
  }

  @Override
  public void set(String key, String value, Date expiration) {
    cookies.put(key, value);
  }

  public Map<String, String> getCookies() {
    return Collections.<String, String>unmodifiableMap(cookies);
  }

}
