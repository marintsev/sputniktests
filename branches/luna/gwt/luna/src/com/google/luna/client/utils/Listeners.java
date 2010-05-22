package com.google.luna.client.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
public class Listeners<T> implements Iterable<T> {

  private List<T> listeners = Collections.<T>emptyList();

  public void add(T listener) {
    if (listeners.isEmpty()) {
      listeners = new ArrayList<T>();
    } else if (listeners.contains(listener)) {
      return;
    }
    listeners.add(listener);
  }

  public void remove(T listener) {
    if (listeners.isEmpty())
      return;
    listeners.remove(listener);
  }

  @Override
  public Iterator<T> iterator() {
    return listeners.iterator();
  }

}
