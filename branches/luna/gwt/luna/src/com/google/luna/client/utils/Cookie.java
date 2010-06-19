package com.google.luna.client.utils;

import java.util.Date;
/**
 * A single browser cookie backed by some cookie jar.  Subclass this
 * to get concrete cookie types that handle encoding/decoding of the
 * stored string values.
 */
public abstract class Cookie<T> {

  public static class Factory {

    private final ICookieJar jar;
    private final String name;

    private Factory(ICookieJar jar, String name) {
      this.jar = jar;
      this.name = name;
    }

    Factory(ICookieJar jar) {
      this(jar, null);
    }

    public String getFullName(String path) {
      return (name == null) ? path : name + "." + path;
    }

    public ICookieJar getJar() {
      return this.jar;
    }

    public Factory child(String subname) {
      if (name == null) return new Factory(jar, subname);
      else return new Factory(jar, name + "." + subname);
    }

    public Cookie<Integer> integer(String name) {
      return new Int(this, name);
    }

    public Cookie<String> string(String name) {
      return new Str(this, name);
    }

  }

  private static class Int extends Cookie<Integer> {

    public Int(Factory factory, String name) {
      super(factory, name);
    }

    @Override
    protected Integer decode(String value) {
      return Integer.parseInt(value);
    }

    @Override
    protected String encode(Integer value) {
      return Integer.toString(value);
    }

    @Override
    protected Integer getEmptyValue() {
      return 0;
    }

  }

  public static class Str extends Cookie<String> {

    public Str(Factory domain, String name) {
      super(domain, name);
    }

    @Override
    protected String decode(String value) {
      return value;
    }

    @Override
    protected String encode(String value) {
      return value;
    }

    @Override
    protected String getEmptyValue() {
      return null;
    }

  }

  private final String fullName;
  private final ICookieJar jar;

  protected Cookie(Factory factory, String name) {
    assert factory != null;
    assert name != null;
    this.fullName = factory.getFullName(name);
    this.jar = factory.getJar();
  }

  public void set(T value, Date expiration) {
    jar.set(fullName, encode(value), expiration);
  }

  public void set(T value) {
    set(value, null);
  }

  public T get() {
    String value = jar.get(fullName);
    return (value == null) ? getEmptyValue() : decode(value);
  }

  public void clear() {
    jar.clear(fullName);
  }

  public boolean exists() {
    return jar.exists(fullName);
  }

  protected abstract String encode(T value);
  protected abstract T decode(String value);
  protected abstract T getEmptyValue();

}
