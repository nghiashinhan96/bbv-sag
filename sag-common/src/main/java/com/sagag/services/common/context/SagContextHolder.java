package com.sagag.services.common.context;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

import java.util.Objects;

/**
 * The SAG common context to store some info in per threads.
 */
@UtilityClass
public class SagContextHolder {

  private static final ThreadLocal<SagContext> SAG_CONTEXT_HOLDER =
      new NamedThreadLocal<>("SAG Common Context");

  private static void resetSagContext() {
    SAG_CONTEXT_HOLDER.remove();
  }

  private static void setSagContext(SagContext context) {
    resetSagContext();
    SAG_CONTEXT_HOLDER.set(context);
  }

  public static SagContext getSagContext() {
    SagContext context = SAG_CONTEXT_HOLDER.get();
    if (Objects.isNull(context)) {
      return null;
    }
    return context;
  }

  public static void setRequestId(String requestId) {
    setSagContext(SagContext.builder().requestId(requestId).build());
  }

  public static String getRequestId() {
    SagContext context = SAG_CONTEXT_HOLDER.get();
    return context == null ? StringUtils.EMPTY : context.getRequestId();
  }
}
