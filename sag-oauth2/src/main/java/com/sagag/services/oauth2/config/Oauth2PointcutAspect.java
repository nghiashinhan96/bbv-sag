package com.sagag.services.oauth2.config;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Oauth2PointcutAspect {

  enum JoinPointMode {
    TOKEN_SERVICE_METHOD,
    READ_ACCESS_TOKEN_METHOD
  }

  @Pointcut("execution(* com.sagag.services.oauth2.security.CustomTokenServices.*(..))")
  public void tokenServiceMethod() { // default implementation ignored
  }

  @Pointcut("execution(* org.springframework.security.oauth2.provider.token.TokenStore.*(..))")
  public void readAccessTokenMethod() { // default implementation ignored
  }

  /**
   * Controls the entry point cut for service methods calling.
   * 
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("tokenServiceMethod()")
  public Object tokenServiceProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.TOKEN_SERVICE_METHOD);
  }

  /**
   * Controls the entry point cut for service methods calling.
   * 
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("readAccessTokenMethod()")
  public Object accessTokenServiceProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.READ_ACCESS_TOKEN_METHOD);
  }

  private static Object executeProceedingJointPoint(final ProceedingJoinPoint joinPoint,
      final JoinPointMode type) throws Throwable {
    final long start = System.currentTimeMillis();
    log.debug(findStartingMessage(type), joinPoint.toShortString());
    final Object output = joinPoint.proceed();
    final long end = (System.currentTimeMillis() - start);
    log.debug(findEndingMessage(type), end, joinPoint.toShortString());
    return output;
  }

  private static String findStartingMessage(final JoinPointMode type) {
    switch (type) {
      case TOKEN_SERVICE_METHOD:
        return "|-> Starting processing CustomTokenService ({} miliseconds), {}";
      case READ_ACCESS_TOKEN_METHOD:
        return "|-> Starting processing CachedTokenStore {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }

  private static String findEndingMessage(final JoinPointMode type) {
    switch (type) {
      case TOKEN_SERVICE_METHOD:
        return "|-> Ending processing CustomTokenService ({} miliseconds), {}";
      case READ_ACCESS_TOKEN_METHOD:
        return "|-> Ending processing CachedTokenStore ({} miliseconds), {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }
}
