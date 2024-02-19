package com.hyeon.backend.utils;

public class SystemUtils {

  public static final String getOSName() {
    return System.getProperty("os.name").toLowerCase();
  }
}
