package com.hyeon.backend.utils;

public class StringUtils {

  public static boolean spaceOrEmptyCheck(String spaceCheck) {
    for (int i = 0; i < spaceCheck.length(); i++) {
      if (spaceCheck.charAt(i) == ' ') return true;
    }
    return spaceCheck.isEmpty();
  }
}
