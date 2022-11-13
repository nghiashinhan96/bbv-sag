package com.sagag.services.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration type of return order errors.
 */
@Getter
@AllArgsConstructor
public enum ReturnOrderErrorType {
  STATUS_CHECK_TIME_OUT, GET_RETURN_ORDER_ORDER_NUMBER_FAILED, STATUS_CHECK_RETURN_ERROR;
}
