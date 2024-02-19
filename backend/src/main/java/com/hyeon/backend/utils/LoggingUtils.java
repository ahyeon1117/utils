package com.hyeon.backend.utils;

import com.hyeon.backend.enums.LoggingId;

public class LoggingUtils {

  /**
   * generate UUID.
   * @param com.hyeon.backend.enums.LoggingId	loggingId
   * @return	String uuid
   */
  public static String generateUUID(LoggingId loggingId) {
    return loggingId.getValue() + "-" + System.currentTimeMillis();
  }
}
