package com.sagag.services.ax.exception.translator;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.exception.AxExternalException;

@Component
public class AxCustomerInternalServerExceptionTranslator
    extends DefaultAxExternalExceptionTranslator {

  @Override
  public AxExternalException apply(Exception ex) {
    return getUnauthorizedRequestException(ex)
        .orElse(new AxCustomerException((RestClientResponseException) ex)) ;
  }
}
