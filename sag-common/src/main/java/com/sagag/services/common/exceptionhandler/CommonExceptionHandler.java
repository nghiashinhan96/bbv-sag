package com.sagag.services.common.exceptionhandler;

import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.ws.client.WebServiceIOException;

/**
 * Common REST APIs exception handler.
 */
@Slf4j
public class CommonExceptionHandler {

  /* 4xx Client Error */
  /**
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ UnsupportedOperationException.class, ValidationException.class,
      IllegalArgumentException.class })
  @ResponseBody
  public RestErrorDto handleBadRequestException(final RuntimeException ex) {
    log.warn("Bad request exception", ex);
    return RestErrorDto.buildBadRequestResponse(ex.getMessage());
  }

  /**
   * {@code 401 Unauthorized}.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({ UnauthorizedUserException.class })
  @ResponseBody
  public RestErrorDto handleUnauthorizedUserException(final UnauthorizedUserException ex) {
    log.warn("Unauthorized access exception", ex);
    return RestErrorDto.buildUnauthorizedResponse(ex.getMessage());
  }

  /**
   * {@code 403 Forbidden}.
   */
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseBody
  public RestErrorDto handleForbiddenException(final AccessDeniedException ex) {
    log.warn("Access forbidden exception", ex);
    return RestErrorDto.buildForbiddenResponse(ex.getMessage());
  }

  /**
   * {@code 404 Not Found}.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResultNotFoundException.class)
  @ResponseBody
  public RestErrorDto handleSearchResultNotFoundException(final ResultNotFoundException ex) {
    log.debug("search result not found exception");
    return RestErrorDto.buildNotFoundResultResponse(ex.getMessage());
  }

  /**
   * {@code 404 Not Found}.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ UsernameNotFoundException.class })
  @ResponseBody
  public RestErrorDto handleUsernameNotFoundException(final UsernameNotFoundException ex) {
    log.warn("Username not found", ex);
    return RestErrorDto.buildUserNotFoundResponse(ex.getMessage());
  }

  /**
   * {@code 405 Method Not Allowed}.
   */
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
  @ResponseBody
  public RestErrorDto handleHttpRequestMethodNotSupportedException(
      final HttpRequestMethodNotSupportedException ex) {
    log.error("Method not supported exception", ex);
    return RestErrorDto.buildMethodNotAllowedResponse(ex.getMessage());
  }

  /**
   * {@code 408 Request Timeout}.
   */
  @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
  @ExceptionHandler({ SocketTimeoutException.class, ConnectTimeoutException.class })
  @ResponseBody
  public RestErrorDto handleRequestTimeOutException(final IOException ex) {
    log.error("Request timeout exception", ex);
    return RestErrorDto.buildTimeoutResponse(ex.getMessage());
  }

  /**
   * {@code 408 Request Timeout}.
   */
  @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
  @ExceptionHandler({ WebServiceIOException.class })
  @ResponseBody
  public RestErrorDto handleRequestTimeOutException(final WebServiceIOException ex) {
    log.error("Request timeout exception", ex);
    return RestErrorDto.buildTimeoutResponse(ex.getMessage());
  }

  /* 5xx Server Error */
  /**
   * {@code 500 Internal Server Error}.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ ServiceException.class })
  @ResponseBody
  public RestErrorDto handleServiceException(final ServiceException ex) {
    log.error(ex.toLogMessage(), ex.getCause());
    return RestErrorDto.buildInternalServerResponse(ex.getKey(), ex.getMessage());
  }

  /**
   * {@code 500 Internal Server Error}.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ Exception.class, Throwable.class })
  @ResponseBody
  public RestErrorDto handleUnknownException(final Throwable ex) {
    log.error("Unknown error. Please check the case and handle it:", ex);
    return RestErrorDto.buildUnkownExceptionResponse();
  }
}
