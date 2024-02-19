package com.hyeon.backend.utils;

public class Formatter {

  public static String formatJsonWithKey(String key, String value) {
    return "{ " + "\"" + key + "\"" + " : " + value + "}";
  }
}
