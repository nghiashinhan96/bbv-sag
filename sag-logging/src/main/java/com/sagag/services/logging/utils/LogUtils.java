package com.sagag.services.logging.utils;

import lombok.experimental.UtilityClass;

import org.slf4j.MDC;

@UtilityClass
public class LogUtils {

  private static final String SLF4J_REQUEST_ID = "requestID";

  private static final String SLF4J_USER = "User";

  public void putUser(String username) {
    MDC.put(SLF4J_USER, username);
  }

  public String getRequestId() {
    return MDC.get(SLF4J_REQUEST_ID);
  }

  public void putRequestId(String requestId) {
    MDC.put(SLF4J_REQUEST_ID, requestId);
  }

  public void removeRequestId() {
    MDC.remove(SLF4J_REQUEST_ID);
  }

}
