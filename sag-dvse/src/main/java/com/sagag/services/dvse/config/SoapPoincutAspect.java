package com.sagag.services.dvse.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Configuration
@Slf4j
@EnableAspectJAutoProxy
public class SoapPoincutAspect {

  enum JoinPointMode {
    END_POINT_METHOD, SERVICE_METHOD, SCHEDULE_METHOD
  }

  @Pointcut("execution(public * com.sagag.services.dvse.endpoint.*Endpoint.*(..))")
  public void endpointMethod() { /* intentionally blank */ }

  @Pointcut("execution(public * com.sagag..*ServiceImpl.*(..))")
  public void serviceMethod() { /* intentionally blank */ }

  @Pointcut("execution(public * com.sagag..*ScheduleTasks.*(..))")
  public void scheduleMethod() { /* intentionally blank */ }

  /**
   * Controls the entry point cut for controller methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("endpointMethod()")
  public Object controllerProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.END_POINT_METHOD);
  }

  /**
   * Controls the entry point cut for service methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("serviceMethod()")
  public Object serviceProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.SERVICE_METHOD);
  }

  /**
   * Controls the entry point cut for schedule methods calling.
   *
   * @param joinPoint the point cut to control
   * @return the output reference of method invocation
   * @throws Throwable exception when program fails.
   */
  @Around("scheduleMethod()")
  public Object scheduleProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.SCHEDULE_METHOD);
  }

  private static Object executeProceedingJointPoint(final ProceedingJoinPoint joinPoint,
      final JoinPointMode type) throws Throwable {
    final long start = System.currentTimeMillis();
    log.debug(findStartingMessage(type), joinPoint.toShortString());

    final Object output = joinPoint.proceed();

    log.debug(findEndingMessage(type), System.currentTimeMillis() - start,
        joinPoint.toShortString());
    return output;
  }

  private static String findStartingMessage(final JoinPointMode type) {
    switch (type) {
      case END_POINT_METHOD:
        return "|-> Starting end point {}";
      case SERVICE_METHOD:
        return "\t|-> Starting service {}";
      case SCHEDULE_METHOD:
        return "\t|-> Starting schedule {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }

  private static String findEndingMessage(final JoinPointMode type) {
    switch (type) {
      case END_POINT_METHOD:
        return "|-> Ending end point({} ms), {}";
      case SERVICE_METHOD:
        return "\t|-> Ending service({} ms), {}";
      case SCHEDULE_METHOD:
        return "\t|-> Ending schedule({} ms), {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }
}
