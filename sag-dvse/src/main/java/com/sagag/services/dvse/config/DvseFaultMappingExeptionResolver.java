package com.sagag.services.dvse.config;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * The exception resolver to handle trace error log.
 * </p>
 *
 *
 */
@Slf4j
public class DvseFaultMappingExeptionResolver extends SoapFaultMappingExceptionResolver {

  @Override
  protected void customizeFault(Object endpoint, Exception exception,
      SoapFault fault) {
    log.error("Exception: ", exception);
  }

}
