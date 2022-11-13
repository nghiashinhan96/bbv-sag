package com.sagag.eshop.service.exception;

import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.Map;

/**
 * Invalid User profile exception.
 */
@EqualsAndHashCode(callSuper = false)
public class InvalidUserProfileException extends ValidationException {

  private static final long serialVersionUID = -3199312317425225402L;

  private final UserProfileDto userProfile;

  public InvalidUserProfileException(final UserProfileDto userProfile) {
    super(String.format("Invalid user profile for username %s.", userProfile.getUserName()));
    this.userProfile = userProfile;
    setMoreInfos(buildMoreInfos());
  }

  @Override
  public Map<String, Object> buildMoreInfos() {
    return Collections.singletonMap(INFO_KEY_USERNAME, userProfile.getUserName());
  }

  @Override
  public String getCode() {
    return UserErrorCase.UE_IUP_001.code();
  }

  @Override
  public String getKey() {
    return UserErrorCase.UE_IUP_001.key();
  }
}
