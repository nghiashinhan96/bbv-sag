package com.sagag.services.common.aspect;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@EnableAspectJAutoProxy
public class LogExecutedTimeAspect {

  private static final String DF_LOG_MSG = "{} executed in {} ms";

  private static final String ALTERNATIVE_LOG_MSG_PATTERN = "%s executed in {} ms";

  @Around("@annotation(LogExecutionTime)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    final long start = System.currentTimeMillis();
    try {
      return joinPoint.proceed();
    } finally {
      if (!logExecutionInfoFromAnnotation(joinPoint, start)) {
        log.debug(DF_LOG_MSG, joinPoint.toShortString(), System.currentTimeMillis() - start);
      }
    }
  }

  private static boolean logExecutionInfoFromAnnotation(ProceedingJoinPoint joinPoint, long start) {
    final LogExecutionTime curAnnotation = AnnotationUtils.getAnnotation(
        ((MethodSignature) joinPoint.getSignature()).getMethod(), LogExecutionTime.class);
    if (curAnnotation == null || StringUtils.isBlank(curAnnotation.value())) {
      return false;
    }

    if (!curAnnotation.infoMode()) {
      log.debug(defaultLogMessage(curAnnotation.value(), joinPoint.toShortString()),
          System.currentTimeMillis() - start);
    } else {
      log.info(defaultLogMessage(curAnnotation.value(), joinPoint.toShortString()),
          System.currentTimeMillis() - start);
    }
    return true;
  }

  private static String defaultLogMessage(String valueLogMsg, String shortMethodName) {
    if (StringUtils.isEmpty(valueLogMsg)) {
      return String.format(ALTERNATIVE_LOG_MSG_PATTERN, shortMethodName);
    }
    return valueLogMsg;
  }

}
