package com.sagag.services.rest.exceptionhandler;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UsernameDuplicationException;
import com.sagag.services.ax.exception.AxBadRequestException;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxResultNotFoundException;
import com.sagag.services.ax.exception.AxTimeoutException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.exceptionhandler.CommonExceptionHandler;
import com.sagag.services.common.exceptionhandler.RestErrorDto;
import com.sagag.services.elasticsearch.exception.ResultOverLimitException;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.VersionValidationException;
import com.sagag.services.service.exception.coupon.CouponValidationException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST APIs exception handler.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RestExceptionHandler extends CommonExceptionHandler {

  /**
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ AxBadRequestException.class, AxCustomerException.class })
  @ResponseBody
  public RestErrorDto handleAxBadRequestException(final AxExternalException ex) {
    log.warn("Bad request from AX SWS", ex);
    return RestErrorDto.buildBadRequestResponse(ex.getErrorCode(), ex.getMessage());
  }

  /**
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ ResultOverLimitException.class})
  @ResponseBody
  public RestErrorDto handleTyresOverLimitException(final ResultOverLimitException ex) {
    return RestErrorDto.buildBadRequestResponse(ex.getCode(), ex.getMessage());
  }

  /**
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ MailException.class })
  @ResponseBody
  public RestErrorDto handleUnknowException(final MailException ex) {
    log.error("Sending mail has problem", ex.getCause());
    return RestErrorDto.buildBadRequestResponse("email_exception", ex.getMessage());
  }

  /**
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ CouponValidationException.class })
  @ResponseBody
  public RestErrorDto handleCouponValidationException(final CouponValidationException ex) {
    log.warn(ex.toLogMessage(), ex);
    return new RestErrorDto(HttpStatus.BAD_REQUEST, ex.getKey())
        .message(StringUtils.defaultIfBlank(ex.getMessage(), RestErrorDto.DF_BAD_REQUEST))
        .businessCode(ex.getCode()).moreInfos(ex.getMoreInfos());
  }

  /**
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ UserValidationException.class, VersionValidationException.class,
      ErpCustomerNotFoundException.class,
      UsernameDuplicationException.class,
      ValidationException.class})
  @ResponseBody
  public RestErrorDto handleBadRequestServiceException(final ServiceException ex) {
    log.warn(ex.toLogMessage(), ex);
    return RestErrorDto.buildBadRequestResponse(ex.getKey(), ex.getMessage());
  }

  /**
   * {@code 404 Not Found}.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(AxResultNotFoundException.class)
  @ResponseBody
  public RestErrorDto handleSearchResultNotFoundException(final AxResultNotFoundException ex) {
    log.debug("Search result not from AX SWS", ex);
    return RestErrorDto.buildNotFoundResultResponse(ex.getMessage());
  }

  /**
   * {@code 500 Internal Server Error}.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({ AxExternalException.class })
  @ResponseBody
  public RestErrorDto handleAxExternalException(final AxExternalException ex) {
    log.error("The AX SWS got error.", ex);
    return RestErrorDto.buildInternalServerResponse(ex.getMessageKey(), ex.getMessage());
  }

  /**
   * {@code 408 Request timeout}.
   */
  @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
  @ExceptionHandler(AxTimeoutException.class)
  @ResponseBody
  public RestErrorDto handleAxRequestTimeOutException(final AxTimeoutException ex) {
    log.debug("Request to AX SWS timeout", ex);
    return RestErrorDto.buildTimeoutResponse(ex.getMessage());
  }
}
