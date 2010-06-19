package com.google.luna.client.utils;

import java.util.Date;

import com.google.gwt.user.client.Cookies;
import com.google.luna.client.utils.Cookie.Factory;
/**
 * A cookie jar representing real browser cookies.
 */
public class BrowserCookieJar implements ICookieJar {

  @Override
  public void clear(String key) {
    Cookies.removeCookie(key);
  }

  @Override
  public String get(String key) {
    return Cookies.getCookie(key);
  }

  @Override
  public boolean exists(String key) {
    return Cookies.getCookie(key) != null;
  }

  @Override
  public void set(String key, String value, Date expiration) {
    if (expiration == null) {
      Cookies.setCookie(key, value);
    } else {
      Cookies.setCookie(key, value, expiration);
    }
  }

  @Override
  public Factory factory() {
    return new Cookie.Factory(this);
  }

}
