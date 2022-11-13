package com.sagag.services.tools.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Class for handling throw MDM not found external customer exception.
 *
 */
public class MdmCustomerNotFoundException extends ServiceException {

  private static final long serialVersionUID = 5639543591228706189L;

  private String extCustId;

  public MdmCustomerNotFoundException(String extCustId) {
    super("Not found dvse customer id = " + extCustId);
    this.extCustId = extCustId;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public String getCode() {
    return "CE_ECN_001";
  }

  @Override
  public String getKey() {
    return "NOT_FOUND_EXTERNAL_CUSTOMER";
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap("INFO_KEY_EXTCUSTID", extCustId);
  }
}
