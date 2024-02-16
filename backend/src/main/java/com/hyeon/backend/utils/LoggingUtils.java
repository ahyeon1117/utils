package com.hyeon.backend.utils;

import com.hyeon.backend.enums.LoggingId;

public class LoggingUtils {

  public String generateUUID(LoggingId loggingId) {
    return loggingId.getValue() + "-" + System.currentTimeMillis();
  }
}
