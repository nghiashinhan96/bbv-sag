package com.sagag.services.admin.exceptionhandler;

import com.sagag.eshop.service.exception.CustomerNotFoundException;
import com.sagag.eshop.service.exception.MessageTimeOverlapException;
import com.sagag.services.ax.exception.AxBadRequestException;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.exception.AxExternalException;
import com.sagag.services.ax.exception.AxResultNotFoundException;
import com.sagag.services.common.exception.CsvServiceException;
import com.sagag.services.common.exception.CsvServiceException.CsvErrorCase;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.exceptionhandler.CommonExceptionHandler;
import com.sagag.services.common.exceptionhandler.RestErrorDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
public class AdminRestExceptionHandler extends CommonExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ MessageTimeOverlapException.class })
  @ResponseBody
  public RestErrorDto handleMessageOverlapException(final MessageTimeOverlapException ex) {
    log.error(ex.toLogMessage(), ex.getCause());
    return RestErrorDto.buildBadRequestResponse(ex.getKey(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ CustomerNotFoundException.class })
  @ResponseBody
  public RestErrorDto handleCustomerNotFoundException(final CustomerNotFoundException ex) {
    log.error(ex.toLogMessage(), ex.getCause());
    return RestErrorDto.buildBadRequestResponse(ex.getKey(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ ValidationException.class })
  @ResponseBody
  public RestErrorDto handleValidationException(final ValidationException ex) {
    log.error(ex.toLogMessage(), ex.getCause());
    return RestErrorDto.buildBadRequestResponse(ex.getKey(), ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ CsvServiceException.class })
  @ResponseBody
  public RestErrorDto handleCsvTypeMismatchException(final CsvServiceException ex) {
    log.error(ex.getMessage(), ex.getCause());
    return RestErrorDto.buildBadRequestResponse(CsvErrorCase.CSV_ERR_001.key(),
        ex.getMessage());
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
   * {@code 400 Bad Request}.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({ AxBadRequestException.class, AxCustomerException.class })
  @ResponseBody
  public RestErrorDto handleAxBadRequestException(final AxExternalException ex) {
    log.warn("Bad request from AX SWS", ex);
    return RestErrorDto.buildBadRequestResponse(ex.getErrorCode(), ex.getMessage());
  }
}
