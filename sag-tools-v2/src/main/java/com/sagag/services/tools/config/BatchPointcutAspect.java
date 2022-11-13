package com.sagag.services.tools.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class BatchPointcutAspect {

  enum JoinPointMode {
    JOB, IMPLEMENTATION
  }

  @Pointcut("execution(public * com.sagag.services.tools..*JobConfig.*(..))")
  public void jobMethod() { }

  @Pointcut("execution(* com.sagag.services.tools.batch..*(..))")
  public void subImplementationMethod() { }

  @Around("jobMethod()")
  public Object jobProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.JOB);
  }

  @Around("subImplementationMethod()")
  public Object subImplementationProfile(final ProceedingJoinPoint joinPoint) throws Throwable {
    return executeProceedingJointPoint(joinPoint, JoinPointMode.IMPLEMENTATION);
  }

  private static Object executeProceedingJointPoint(final ProceedingJoinPoint joinPoint,
    final JoinPointMode type) throws Throwable {
    final long start = System.currentTimeMillis();
    log.info(findStartingMessage(type), joinPoint.toShortString());
    final Object output = joinPoint.proceed();
    final long end = (System.currentTimeMillis() - start);
    log.info(findEndingMessage(type), end, joinPoint.toShortString());
    return output;
  }

  private static String findStartingMessage(final JoinPointMode type) {
    switch (type) {
      case JOB:
        return "|-> Starting Job {}";
      case IMPLEMENTATION:
        return "\t|-> Starting implementation {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }

  private static String findEndingMessage(final JoinPointMode type) {
    switch (type) {
      case JOB:
        return "|-> Ending Job({} ms), {}";
      case IMPLEMENTATION:
        return "\t|-> Ending implementation ({} ms), {}";
      default:
        throw new IllegalArgumentException("Not supported join point.");
    }
  }
}
