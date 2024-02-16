package com.hyeon.backend.aspect;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Pointcut("@annotation(com.hyeon.backend.aspect.annotation.Timer)") //Timer 어노테이션이 붙은 메서드에만 적용
  private void enableTimer() {}

  @Around("enableTimer()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();

    String pattern = "yyyy-MM-dd HH:mm:ss.SSS";

    String start = LocalDateTime
      .now()
      .format(DateTimeFormatter.ofPattern(pattern))
      .toString();

    stopWatch.start();
    Object result = joinPoint.proceed(); // 조인포인트의 메서드 실행
    stopWatch.stop();

    String end = LocalDateTime
      .now()
      .format(DateTimeFormatter.ofPattern(pattern))
      .toString();

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = signature.getMethod().getName();
    log.info(
      "class : {} | method : {} | start : {} | end : {} | duration : {}ms",
      className,
      methodName,
      start,
      end,
      stopWatch.getTotalTimeMillis()
    );
    return result;
  }
}
