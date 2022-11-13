package com.sagag.services.dvse.config;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.sagag.services.common.cors.SagCorsFilter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SoapCorsFilter extends SagCorsFilter {

  private static final String[] CORS_SUPPORT_METHODS = new String[] {
      HttpMethod.GET.name(),
      HttpMethod.POST.name() };

  @Override
  public void updateResponseHeaders(HttpServletResponse response) {
    log.debug("Updating response headers in CorFilter at SOAP services.");
    super.updateResponseHeaders(response);
    response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
        StringUtils.join(CORS_SUPPORT_METHODS, ","));
  }

}
