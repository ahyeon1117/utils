package com.hyeon.backend.enums;

import java.util.Arrays;

public enum Status {
  CONNECTED(1),
  DISCONNECTED(2);

  Integer value;

  private Status(Integer value) {
    this.value = value;
  }

  public static Status enumOf(int n) {
    return Arrays
      .stream(Status.values())
      .filter(t -> t.getValue() == n)
      .findAny()
      .orElse(null);
  }

  public Integer getValue() {
    return this.value;
  }
}
