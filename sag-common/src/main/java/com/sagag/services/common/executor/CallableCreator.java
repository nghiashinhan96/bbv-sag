package com.sagag.services.common.executor;

import com.sagag.services.common.utils.RequestContextUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface CallableCreator<T, U> {

  /**
   * Creates the async callable.
   *
   * @param input the generic input
   * @param objects the additional objects
   *
   * @return the callable object
   */
  Callable<U> create(T input, Object... objects);

  static void setupThreadContext(Object... objects) {
    if (ArrayUtils.isEmpty(objects)
        || !TypeUtils.isInstance(objects[0], ServletRequestAttributes.class)) {
      return;
    }
    RequestContextUtils.setupThreadContext((ServletRequestAttributes) objects[0]);
  }
}
