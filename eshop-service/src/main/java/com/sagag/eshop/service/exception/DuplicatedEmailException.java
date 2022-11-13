package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.ServiceException;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Map;

/**
 * Class for handling throw duplicated email exception.
 *
 */
@EqualsAndHashCode(callSuper = false)
public class DuplicatedEmailException extends ServiceException {

  private static final long serialVersionUID = 1305964749317743328L;
  private final String email;

  public DuplicatedEmailException(String email) {
    super("Duplicated email is :" + email);
    this.email = email;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public String getCode() {
    return "UE_DEE_001";
  }

  @Override
  public String getKey() {
    return "DUPLICATED_EMAIL";
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap("INFO_KEY_DUPLICATED_EMAIL", email);
  }
}
