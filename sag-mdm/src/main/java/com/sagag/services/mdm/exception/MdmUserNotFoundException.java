package com.sagag.services.mdm.exception;

import com.sagag.services.common.exception.ServiceException;

import java.util.Collections;
import java.util.Map;

/**
 * Class for handling throw MDM not found external user exception.
 *
 */
public class MdmUserNotFoundException extends ServiceException {

  private static final long serialVersionUID = -6879678636162612809L;

  private String username;

  public MdmUserNotFoundException(String username) {
    super(String.format("Not found any external user info with username = %s", username));
    this.username = username;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public String getCode() {
    return "UE_EUN_001";
  }

  @Override
  public String getKey() {
    return "NOT_FOUND_EXTERNAL_USER";
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap(INFO_KEY_USERNAME, username);
  }
}
