package com.hyeon.backend.enums;

public enum LoggingId {
  ONE("ONE"),
  TWO("TWO"),
  THREE("THREE");

  private final String value;

  LoggingId(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
