package com.google.luna.client;

import com.google.gwt.user.client.Window.Location;
import com.google.luna.client.logic.ILogView;
import com.google.luna.client.logic.ToplevelPresenter;

import java.util.EnumSet;

public class Log {

  private static boolean isEnabled = "true".equals(Location.getParameter("showLog"));

  public static void setEnabled(boolean value) {
    isEnabled = value;
  }

  public static boolean isEnabled(Component comp) {
    return isEnabled() && shouldLogComponent(comp);
  }

  public static boolean isEnabled() {
    return isEnabled;
  }

  public enum Component {

    COOKIES("Cookies"),
    CONTROL_MODE("ControlMode");

    private final String name;

    private Component(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

  }

  private static EnumSet<Component> components;
  private static boolean shouldLogComponent(Component comp) {
    if (components == null) {
      EnumSet<Component> set = EnumSet.noneOf(Component.class);
      for (Component each : Component.values()) {
        String paramName = "log" + each.getName();
        if ("true".equals(Location.getParameter(paramName))) {
          set.add(each);
        }
      }
      components = set;
    }
    return components.contains(comp);
  }

  public static void addMessage(Log.Component component, String message) {
    if (isEnabled) {
      ToplevelPresenter toplevel = Luna.getToplevel();
      if (toplevel != null) {
        ILogView log = toplevel.getLog();
        if (log != null) {
          log.addMessage(component, message);
        }
      }
    }
  }

}
