package com.hyeon.backend.utils;

import java.util.regex.Pattern;

public class RegexUtils {

  public static final Pattern REG_NO_PATTERN = Pattern.compile(
    "^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$"
  );

  public static final String REG_NO_PATTERN_STRING =
    "^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$";

  public static boolean regNoCheck(String regNo) {
    return REG_NO_PATTERN.matcher(regNo).matches();
  }
}
