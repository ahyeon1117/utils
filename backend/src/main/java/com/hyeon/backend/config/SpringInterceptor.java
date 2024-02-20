package com.hyeon.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringInterceptor implements HandlerInterceptor {

  @Override
  public void afterCompletion(
    HttpServletRequest request,
    HttpServletResponse response,
    Object handler,
    Exception ex
  ) {}

  @Override
  public boolean preHandle(
    HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse,
    Object o
  ) {
    log.info(
      "{} {}",
      httpServletRequest.getMethod(),
      httpServletRequest.getRequestURI()
    );
    return true;
  }

  @Override
  public void postHandle(
    HttpServletRequest httpServletRequest,
    HttpServletResponse httpServletResponse,
    Object o,
    ModelAndView modelAndView
  ) throws IOException {}
}
