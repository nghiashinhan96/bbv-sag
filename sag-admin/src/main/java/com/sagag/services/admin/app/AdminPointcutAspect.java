package com.sagag.services.admin.app;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AdminPointcutAspect {

  enum JoinPointMode {
    CONTROLLER_METHOD, SERVICE_METHOD, MAPSTORE_METHOD
  }

  @Pointcut("execution(public * com.sagag.services.admin.controller..*Controller.*(..))")
  public void controllerMethod() { // default implementation ignored
  }

  @Pointcut("execution(public * com.sagag..*ServiceImpl.*(..))")
  public void businessMethod() { // default implementation ignored
  }

  @Pointcut("execution(* com.hazelcast.core.MapStore.*(..))")
  public void mapstoreMethod() { // default implementation ignored
  }

  /**
   * Controls the entry point cut for controller methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("controllerMethod()")
  public Object controllerProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
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
}
