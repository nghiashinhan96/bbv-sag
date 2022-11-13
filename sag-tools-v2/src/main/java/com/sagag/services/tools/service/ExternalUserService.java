package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.mdm.ExternalUserDto;
import com.sagag.services.tools.domain.target.ExternalUser;

public interface ExternalUserService {

  ExternalUser addExternalUser(ExternalUserDto externalUserDto);

  /**
   * Checks if external externalUsername existed .
   *
   * @param externalUsername the externalUsername to check
   * @return <code>true</code> if externalUsername existed
   */
  boolean isUsernameExisted(String externalUsername);
}
