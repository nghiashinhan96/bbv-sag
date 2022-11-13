package com.sagag.eshop.service.exception;

import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.exception.ValidationException;

import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception class for validating username duplication at affiliate scope.
 */
@EqualsAndHashCode(callSuper = false)
public class UsernameDuplicationException extends ValidationException {

  private static final long serialVersionUID = 1305964749317743328L;

  private final String username;

  private final String affiliate;

  public UsernameDuplicationException(String username, String affiliate) {
    super(String.format("Duplicated username %s in affiliate %s!", username, affiliate));
    this.username = username;
    this.affiliate = affiliate;
    setMoreInfos(buildMoreInfos()); // to allow the client get those info
  }

  @Override
  public String getCode() {
    return UserErrorCase.UE_DUA_001.code();
  }

  @Override
  public String getKey() {
    return UserErrorCase.UE_DUA_001.key();
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    final Map<String, Object> moreInfos = new HashMap<>();
    moreInfos.put(INFO_KEY_USERNAME, username);
    moreInfos.put(INFO_KEY_AFFILIATE, affiliate);
    return moreInfos;
  }
}
