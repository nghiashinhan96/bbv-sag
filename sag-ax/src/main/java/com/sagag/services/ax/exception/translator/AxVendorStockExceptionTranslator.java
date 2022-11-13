package com.sagag.services.ax.exception.translator;

import org.springframework.stereotype.Component;

import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxVendorStockNotFoundException;

@Component
public class AxVendorStockExceptionTranslator extends DefaultAxExternalExceptionTranslator {

  @Override
  public AxExternalException apply(Exception ex) {
    return getUnauthorizedRequestException(ex).orElse(new AxVendorStockNotFoundException(ex));
  }
}
