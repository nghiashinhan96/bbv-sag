package com.sagag.services.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ServiceException;

import lombok.AllArgsConstructor;

/**
 * Exception class for changing order status by sales.
 */
public class OrderStatusException extends ServiceException {

  private static final long serialVersionUID = -7874065832866768756L;

  public OrderStatusException(OrderStatusCase error, String message) {
    super(message);
    setCode(error.code());
    setKey(error.key());
  }

  public OrderStatusException(OrderStatusCase error, Throwable cause) {
    super(cause);
    setCode(error.code());
    setKey(error.key());
  }

  /** Error case for changing order status process. */
  @AllArgsConstructor
  public enum OrderStatusCase implements IBusinessCode{

    COS_STO_001("CHANGED_ORDER_STATUS_FAILED"),
    COS_STO_002("AX_CHANGED_STATUS_IS_FALSE");

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
