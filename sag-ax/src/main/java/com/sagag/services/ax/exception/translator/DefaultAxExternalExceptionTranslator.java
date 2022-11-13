package com.sagag.services.ax.exception.translator;

import java.util.Optional;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import com.sagag.services.ax.exception.AxBadRequestException;
import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxResultNotFoundException;
import com.sagag.services.ax.exception.AxTimeoutException;
import com.sagag.services.ax.exception.AxUnauthorizedRequestException;
import com.sagag.services.ax.exception.ErrorInfo;

@Component
public class DefaultAxExternalExceptionTranslator extends AxExternalExceptionTranslator {

  @Override
  public AxExternalException apply(Exception ex) {
    return buildException(ex);
  }

  private static AxExternalException buildException(Exception ex) {
    if (TypeUtils.isInstance(ex, ResourceAccessException.class)) {
      return AxExternalException.axInternalServerError(ex.getMessage());
    }
    final RestClientResponseException restClientEx = (RestClientResponseException) ex;
    final ErrorInfo error = errorInfoConverter().apply(restClientEx.getResponseBodyAsByteArray());
    switch (HttpStatus.resolve(restClientEx.getRawStatusCode())) {
      case UNAUTHORIZED:
        return new AxUnauthorizedRequestException(error.getErrorMessage());
      case NOT_FOUND:
        return new AxResultNotFoundException(error.getErrorMessage());
      case BAD_REQUEST:
        return new AxBadRequestException(error.getErrorMessage());
      case GATEWAY_TIMEOUT:
        return AxTimeoutException.axGatewayTimeoutException(error.getErrorMessage());
      case REQUEST_TIMEOUT:
        return AxTimeoutException.requestTimeoutException(error.getErrorMessage());
      default:
        return AxExternalException.axInternalServerError(error.getErrorMessage());
    }
  }

  protected Optional<AxExternalException> getUnauthorizedRequestException(Exception ex) {
    return Optional.of(buildException(ex))
        .filter(aEx -> TypeUtils.isInstance(aEx, AxUnauthorizedRequestException.class));
  }
}
