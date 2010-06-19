package com.google.luna.client.utils;

import java.util.Date;

public interface ICookieJar {

  public void set(String key, String value, Date expiration);
  public String get(String key);
  public boolean exists(String key);
  public void clear(String key);

  public Cookie.Factory factory();

}
