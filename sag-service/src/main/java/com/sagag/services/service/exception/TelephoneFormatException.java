package com.sagag.services.service.exception;

import com.sagag.services.common.exception.ValidationException;

import java.util.Collections;
import java.util.Map;

/**
 * Telephone format exception class definition.
 */
public class TelephoneFormatException extends ValidationException {

  private static final long serialVersionUID = 7986297977206416709L;

  private String telephone;

  /**
   * Construct the telephone exception instant from wrong format telephone.
   * 
   * @param telephone the wrong format telephone
   */
  public TelephoneFormatException(final String telephone) {
    super(String.format("Invalid Telephone number = %s", telephone));
    this.telephone = telephone;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap("INFO_KEY_TEL", telephone);
  }
}
