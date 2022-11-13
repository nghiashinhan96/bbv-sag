package com.sagag.services.rest.app;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.logging.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

/**
 * Aspect IOC logging configuration class.
 */
@Aspect
@Component
@Slf4j
public class RestPointcutAspect {

  private static final long TIME_THRESHOLD_API = 10000L;

  enum JoinPointMode {
    CONTROLLER_METHOD, SERVICE_METHOD, MAPSTORE_METHOD
  }

  @Pointcut("execution(public * com.sagag.services.rest.controller..*Controller.*(..))")
  public void controllerMethod() { /* this is just the declarative point. */ }

  @Pointcut("execution(public * com.sagag..*ServiceImpl.*(..))")
  public void businessMethod() { /* this is just the declarative point. */ }

  @Pointcut("execution(* com.hazelcast.core.MapStore.*(..))")
  public void mapstoreMethod() { /* this is just the declarative point. */ }

  /**
   * Controls the entry point cut for controller methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("controllerMethod()")
  public Object controllerProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    putUser();
    return executeProceedingJointPoint(joinPoint, JoinPointMode.CONTROLLER_METHOD);
  }

  /**
   * Controls the entry point cut for service methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("businessMethod()")
  public Object serviceProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    putUser();
    return executeProceedingJointPoint(joinPoint, JoinPointMode.SERVICE_METHOD);
  }

  /**
   * Controls the entry point cut for mapstore methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("mapstoreMethod()")
  public Object mapstoreProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.MAPSTORE_METHOD);
  }

  private static void putUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2Authentication) {
      putUser((OAuth2Authentication) authentication);
    }
  }

  private static void putUser(OAuth2Authentication authed) {
    if (authed == null || authed.getPrincipal() == null) {
      LogUtils.putUser(StringUtils.EMPTY);
      return;
    }
    UserInfo userInfo = (UserInfo) authed.getPrincipal();
    LogUtils.putUser(String.valueOf(userInfo.getCachedUsername()));
  }

  private static Object executeProceedingJointPoint(final ProceedingJoinPoint joinPoint,
      final JoinPointMode type) throws Throwable {
    final long start = System.currentTimeMillis();
    final String jointShortStr = joinPoint.toShortString();
    log.debug(findStartingMessage(type), jointShortStr);
    final Object output = joinPoint.proceed();
    final long end = (System.currentTimeMillis() - start);
    log.debug(findEndingMessage(type), end, jointShortStr);
    if (exceedThreshold(end)) {
      log.warn("Exceeds threshold({} miliseconds), {}", end, jointShortStr);
    }
    return output;
  }

  private static String findStartingMessage(final JoinPointMode type) {
    switch (type) {
      case CONTROLLER_METHOD:
        return "|-> Starting controller {}";
      case SERVICE_METHOD:
        return "\t|-> Starting service {}";
      case MAPSTORE_METHOD:
        return "\t|-> Starting mapstore {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }

  private static String findEndingMessage(final JoinPointMode type) {
    switch (type) {
      case CONTROLLER_METHOD:
        return "|-> Ending controller({} miliseconds), {}";
      case SERVICE_METHOD:
        return "\t|-> Ending service({} miliseconds), {}";
      case MAPSTORE_METHOD:
        return "\t\t|-> Ending mapstore({} miliseconds), {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }

  private static boolean exceedThreshold(long timeConsuming) {
    return timeConsuming >= TIME_THRESHOLD_API;
  }
}
