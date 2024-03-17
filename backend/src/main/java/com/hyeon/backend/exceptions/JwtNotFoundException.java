package com.hyeon.backend.exceptions;

public class JwtNotFoundException extends NullPointerException {

  public JwtNotFoundException(String s) {
    super(s);
  }
}
