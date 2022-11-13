package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.context.SagContextHolder;
import com.sagag.services.logging.utils.LogUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

/**
 * Utilities for HTTP request context.
 */
@UtilityClass
@Slf4j
public class RequestContextUtils {

  private static void storeRequestIdToLog4JContext(final String requestId, final String requestUri,
      final String requestMethod) {
    try {
      final StringBuilder logBuilder = new StringBuilder();
      if (StringUtils.isNotBlank(requestId)) {
        LogUtils.putRequestId(requestId); // Add requestID to log pattern
        logBuilder.append("The request id is ").append(requestId);
      }
      logBuilder.append(" from request uri is ").append(requestUri);
      logBuilder.append(" with HTTP method is ").append(requestMethod);

      // Add to log debug
      log.debug(logBuilder.toString());
    } catch (final IllegalArgumentException | IllegalStateException ex) {
      LogUtils.removeRequestId();
      log.error("Cannot get requestId from request: {}", ex);
    }
  }

  public static void setupThreadContext(ServletRequestAttributes requestAttributes) {
    log.debug("Setting up thread context");
    if (Objects.isNull(requestAttributes) || Objects.isNull(requestAttributes.getRequest())) {
      log.error("The request attributes is null");
      return;
    }
    final HttpServletRequest request = requestAttributes.getRequest();
    String requestId = request.getHeader(SagConstants.HEADER_X_SAG_REQUEST_ID);
    if (StringUtils.isBlank(requestId)) {
      log.debug("Generate new requestID");
      // If request id in request header is null, we will generate to send to AX
      requestId = UUID.randomUUID().toString();
    }
    log.debug("The requestID to send to AX services is {}", requestId);
    SagContextHolder.setRequestId(requestId);
    storeRequestIdToLog4JContext(requestId, request.getRequestURI(), request.getMethod());
  }

  public static String getRequestId() {
    return SagContextHolder.getRequestId();
  }
}
