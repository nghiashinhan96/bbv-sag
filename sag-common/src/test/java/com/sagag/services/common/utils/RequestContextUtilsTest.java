package com.sagag.services.common.utils;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.logging.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * UT to verify {@link RequestContextUtils}.
 */
public class RequestContextUtilsTest {

  @Test
  public void shouldGenerateNewUuid(){
    final MockHttpServletRequest request =
        new MockHttpServletRequest(HttpMethod.PUT.toString(), "rest/test");
    RequestContextUtils.setupThreadContext(new ServletRequestAttributes(request));

    assertTrue(StringUtils.isNotEmpty(RequestContextUtils.getRequestId()));
  }

  @Test
  public void shouldGetFromRequestHeader() {
    final MockHttpServletRequest request =
        new MockHttpServletRequest(HttpMethod.PUT.toString(), "rest/test");
    final String requestId = UUID.randomUUID().toString();
    request.addHeader(SagConstants.HEADER_X_SAG_REQUEST_ID, requestId);
    RequestContextUtils.setupThreadContext(new ServletRequestAttributes(request));

    assertEquals(requestId, RequestContextUtils.getRequestId());
  }

  @Test
  public void shouldSetupThreadContext(){
    LogUtils.removeRequestId();
    final MockHttpServletRequest request =
        new MockHttpServletRequest(HttpMethod.PUT.toString(), "rest/test");
    final String requestId = UUID.randomUUID().toString();
    request.addHeader(SagConstants.HEADER_X_SAG_REQUEST_ID, requestId);
    RequestContextUtils.setupThreadContext(new ServletRequestAttributes(request));

    assertEquals(requestId, LogUtils.getRequestId());
    assertEquals(requestId, RequestContextUtils.getRequestId());
  }
}
