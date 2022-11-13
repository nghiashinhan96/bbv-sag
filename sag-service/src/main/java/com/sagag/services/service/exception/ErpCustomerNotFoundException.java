package com.sagag.services.service.exception;

import com.sagag.services.common.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception class when the customer not found.
 */
public class ErpCustomerNotFoundException extends ServiceException {

  private static final long serialVersionUID = 7869790506030304213L;

  /** search customer mode enumeration definition. */
  public enum SearchMode {
    CUSTNR, // search customer by number
    TEL, // search customer by telephone
    FREETEXT // search customer by freetext
  }

  private String input;
  private SearchMode mode;

  public ErpCustomerNotFoundException(String input, SearchMode mode) {
    super(buildErrorMessage(input, mode));
    this.input = input;
    this.mode = mode;
    setMoreInfos(buildMoreInfos());
  }

  private static String buildErrorMessage(String input, SearchMode mode) {
    switch (mode) {
      case CUSTNR:
        return String.format("The customer not found by customer number= %s.", input);
      case TEL:
        return String.format("The customer not found by telephone number= %s.", input);
      default: // FREETEXT
        return String.format("The customer not found by freetext = %s.", input);
    }
  }

  @Override
  public String getCode() {
    return "CE_CNF_001";
  }

  @Override
  public String getKey() {
    return "NOT_FOUND_CUSTOMER";
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    final Map<String, Object> moreInfos = new HashMap<>();
    switch (mode) {
      case CUSTNR:
        moreInfos.put("INFO_KEY_CUSTNR", input);
        break;
      case TEL:
        moreInfos.put("INFO_KEY_TEL", input);
        break;
      default: // FREETEXT
        moreInfos.put("INFO_KEY_FREETEXT", input);
        break;
    }
    return moreInfos;
  }
}
