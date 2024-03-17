package com.hyeon.backend.utils;

import java.time.Duration;
import java.util.Map;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class ApiUtils {

  private static final RestTemplate restTemplate = new RestTemplateBuilder()
    .setConnectTimeout(Duration.ofMinutes(2))
    .setReadTimeout(Duration.ofMinutes(2))
    .build();

  public static <S, T> T post(
    String url,
    Map<String, Object> body,
    HttpHeaders headers,
    Class<T> targetClass
  ) {
    HttpEntity<?> request = new HttpEntity<>(body, headers);

    return restTemplate
      .exchange(url, HttpMethod.POST, request, targetClass)
      .getBody();
  }
}
