package com.sagag.services.service.exception;

import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.ax.request.AxOrderRequest;
import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ServiceException;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception class for ordering.
 */
public class OrderException extends ServiceException {

  private static final long serialVersionUID = -7955396368006230941L;

  private final String companyName;

  private final ExternalOrderRequest orderRequest;

  public OrderException(String companyName, ExternalOrderRequest orderRequest,
      Throwable throwable) {
    super(buildErrorMessageWhenOrderingFailed(companyName, orderRequest), throwable);
    this.companyName = companyName;
    this.orderRequest = orderRequest;
    setMoreInfos(buildMoreInfos());
  }

  public OrderException(String companyName, ExternalOrderRequest orderRequest, OrderErrorCase error,
      Throwable throwable) {
    this(companyName, orderRequest, throwable);
    setCode(error.code());
    setKey(error.key());
  }

  private static String buildErrorMessageWhenOrderingFailed(String companyName,
      ExternalOrderRequest orderRequest) {
    if (!(orderRequest instanceof AxOrderRequest)) {
      return StringUtils.EMPTY; // only log when ordering to AX, then the request should be a
                                // AxOrderRequest
    }
    final AxOrderRequest request = (AxOrderRequest) orderRequest;
    return String.format("Error while ordering by company name = %s.%nRequest = %s", companyName,
        stringOf(request));
  }

  private static String stringOf(final AxOrderRequest request) {
    return String.format(
        "CustomerNr = %s, personalNumber = %s, pickupBranchId = %s, salesOriginId = %s, sendMethod = %s",
        request.getCustomerNr(), request.getPersonalNumber(), request.getPickupBranchId(),
        request.getSalesOriginId(), request.getSendMethodCode());
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    if (!(orderRequest instanceof AxOrderRequest)) {
      return Collections.emptyMap();
    }
    final AxOrderRequest request = (AxOrderRequest) orderRequest;
    final Map<String, Object> moreInfos = new HashMap<>();
    moreInfos.put("INFO_KEY_COMPANYNAME", companyName);
    moreInfos.put("INFO_KEY_CUSTNR", request.getCustomerNr());
    moreInfos.put("INFO_KEY_PERSONNR", request.getPersonalNumber());
    moreInfos.put("INFO_KEY_PICKBRANCHID", request.getPickupBranchId());
    moreInfos.put("INFO_KEY_SALESORGID", request.getSalesOriginId());
    moreInfos.put("INFO_KEY_SENDMETHODCODE", request.getSendMethodCode());
    return moreInfos;
  }

  /** Error case for ordering process. */
  public enum OrderErrorCase implements IBusinessCode {

    OE_STO_001("TIMEOUT_ERROR"),
    OE_STO_002("AX_TIMEOUT_ERROR"),
    OE_STO_003("AX_INVALID_COMPANY"),
    OE_STO_004("AX_INVALID_DATA");

    private String key;

    OrderErrorCase(String key) {
      this.key = key;
    }
    
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
