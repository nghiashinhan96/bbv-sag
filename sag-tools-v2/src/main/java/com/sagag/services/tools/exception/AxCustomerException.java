package com.sagag.services.tools.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;

import java.util.stream.Stream;

@Getter
public class AxCustomerException extends AxExternalException {

  private static final long serialVersionUID = -7805130214065812604L;

  public AxCustomerException(AxCustomerErrorCase errorCase, String msg) {
    super(msg);
    setErrorCode(errorCase.code());
    setMessageKey(errorCase.key());
  }

  public AxCustomerException(RestClientResponseException ex) {
    super(ex.getMessage());
    final AxCustomerErrorCase errorCase = getAxCustomerErrorCase(ex);
    setErrorCode(errorCase.code());
    setMessageKey(errorCase.key());
  }

  private AxCustomerErrorCase getAxCustomerErrorCase(RestClientResponseException ex) {
    return Stream.of(AxCustomerErrorCase.values())
        .filter(errorCase -> errorCase.isMatchError(ex)).findFirst()
        .orElse(AxCustomerErrorCase.AC_ISE_001);
  }

  @AllArgsConstructor
  public enum AxCustomerErrorCase implements IBusinessCode, IAxErrorMatcher {

    AC_NFC_001("NOT_FOUND_CUSTOMER") {

      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        return ex instanceof HttpClientErrorException
            && HttpStatus.NOT_FOUND.value() == ex.getRawStatusCode();
      }
    },

    AC_IPR_001("INVALID_PARAMETER_RESPONSE") {

      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        return ex instanceof HttpClientErrorException
            && HttpStatus.BAD_REQUEST.value() == ex.getRawStatusCode();
      }
    },

    AC_ISE_001("INTERNAL_SERVER_ERROR_FROM_AX") {

      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        return ex instanceof HttpServerErrorException;
      }
    },

    AC_PAYMENT_TYPE_001("EMPTY_PAYMENT_TYPE") {
      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        throw new UnsupportedOperationException();
      }
    },

    AC_PAYMENT_TYPE_002("UN_SUPPORT_PAYMENT_TYPE") {
      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        throw new UnsupportedOperationException();
      }
    },

    AC_SALES_ORDER_POOL_001("EMPTY_SALES_ORDER_POOL") {
      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        throw new UnsupportedOperationException();
      }
    },

    AC_SALES_ORDER_POOL_002("UN_SUPPORT_SALES_ORDER_POOL") {
      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        throw new UnsupportedOperationException();
      }
    },

    AC_INVOICE_TYPE_001("EMPTY_INVOICE_TYPE") {
      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        throw new UnsupportedOperationException();
      }
    },

    AC_INVOICE_TYPE_002("UN_SUPPORT_INVOICE_TYPE") {
      @Override
      public boolean isMatchError(RestClientResponseException ex) {
        throw new UnsupportedOperationException();
      }
    };

    private String key;

    @Override
    public String code() {
      return this.name();
    }

    @Override
    public String key() {
      return this.key;
    }

  }

}
