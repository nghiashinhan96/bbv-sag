package com.sagag.services.common.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * General REST error Dto class.
 */
@Data
@JsonPropertyOrder({ "status", "code", "error_code", "message", "additonalinfo" })
public class RestErrorDto {

  private static final String DF_NOT_FOUND = "Oops!. Sorry, Search result not found.";

  private static final String DF_ACCESS_DENIED = "Oops!. Sorry access denied.";

  private static final String DF_INTERNAL_SERVER = "Oops!. Internal server error.";

  private static final String DF_TIME_OUT = "Nope!. I can't wait any longer than 10 seconds";

  public static final String DF_BAD_REQUEST = "Oops!. Bad request.";

  private static final String DF_USER_NAME_NOT_FOUND = "Oops!. Username is not correct.";

  private static final String DF_UNAUTHORIZED = "Oops!. Unauthorized access.";

  private static final String EMPTY_ERROR_CODE = "";

  /** the status at HTTP level. */
  private final HttpStatus status;

  /** the status code at HTTP level. */
  private final int code;

  /**
   * currently, this is implicitly known as the business error message key at application level.
   * This should be the business error code rather than the business error message key.
   */
  @JsonProperty("error_code")
  private final String errorCode;

  /** the error message at application level. Just for logging and debugging. Not multilingual. */
  private String message;

  /** the business error code which is unique at application level. This is for maintainability. */
  private String businessCode;

  /**
   * the additional informations returned from backend during the exception, they can be the
   * arguments to build frontend multilingual message from key.
   */
  private Map<String, Object> moreInfos;

  /**
   * Constructs the error response.
   *
   * @param status the status of the response.
   */
  public RestErrorDto(final HttpStatus status) {
    this(status, EMPTY_ERROR_CODE);
  }

  /**
   * Constructs the error response.
   *
   * @param status the status of the response.
   * @param errorCode the custom error code from SAG business
   */
  public RestErrorDto(final HttpStatus status, final String errorCode) {
    this.status = status;
    this.code = status.value();
    this.errorCode = errorCode;
    this.moreInfos = new HashMap<>();
  }

  /**
   * Builds this error dto with message.
   *
   * @param message the message to append
   * @return the error dto instance
   */
  public RestErrorDto message(final String message) {
    this.message = message;
    return this;
  }

  /**
   * Builds this error dto with error business code.
   *
   * @param businessCode the business code to append
   * @return the error dto instance
   */
  public RestErrorDto businessCode(final String businessCode) {
    this.businessCode = businessCode;
    return this;
  }

  /**
   * Builds this error dto with more information.
   *
   * @param moreInfos more information to append
   * @return the error dto instance
   */
  public RestErrorDto moreInfos(final Map<String, Object> moreInfos) {
    if (!MapUtils.isEmpty(moreInfos)) {
      this.moreInfos.putAll(moreInfos);
    }
    return this;
  }

  public static RestErrorDto buildNotFoundResultResponse(final String message) {
    return new RestErrorDto(HttpStatus.NOT_FOUND)
        .message(StringUtils.defaultIfBlank(message, DF_NOT_FOUND));
  }

  public static RestErrorDto buildUserNotFoundResponse(final String message) {
    return new RestErrorDto(HttpStatus.NOT_FOUND)
        .message(StringUtils.defaultIfBlank(message, DF_USER_NAME_NOT_FOUND));
  }

  public static RestErrorDto buildForbiddenResponse(final String message) {
    return new RestErrorDto(HttpStatus.FORBIDDEN)
        .message(StringUtils.defaultIfBlank(message, DF_ACCESS_DENIED));
  }

  public static RestErrorDto buildUnkownExceptionResponse() {
    return new RestErrorDto(HttpStatus.INTERNAL_SERVER_ERROR).message(DF_INTERNAL_SERVER);
  }

  public static RestErrorDto buildInternalServerResponse(final String errorCode,
      final String message) {
    return new RestErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, errorCode)
        .message(StringUtils.defaultIfBlank(message, DF_INTERNAL_SERVER));
  }

  public static RestErrorDto buildMethodNotAllowedResponse(final String message) {
    return new RestErrorDto(HttpStatus.METHOD_NOT_ALLOWED)
        .message(StringUtils.defaultIfBlank(message, StringUtils.EMPTY));
  }

  public static RestErrorDto buildUnauthorizedResponse(final String message) {
    return new RestErrorDto(HttpStatus.UNAUTHORIZED)
        .message(StringUtils.defaultIfBlank(message, DF_UNAUTHORIZED));
  }

  public static RestErrorDto buildBadRequestResponse(final String message) {
    return new RestErrorDto(HttpStatus.BAD_REQUEST)
        .message(StringUtils.defaultIfBlank(message, DF_BAD_REQUEST));
  }

  public static RestErrorDto buildTimeoutResponse(final String message) {
    return new RestErrorDto(HttpStatus.REQUEST_TIMEOUT)
        .message(StringUtils.defaultIfBlank(message, DF_TIME_OUT));
  }

  public static RestErrorDto buildBadRequestResponse(final String errorCode,
      final String message) {
    return new RestErrorDto(HttpStatus.BAD_REQUEST, errorCode)
        .message(StringUtils.defaultIfBlank(message, DF_BAD_REQUEST));
  }

}
