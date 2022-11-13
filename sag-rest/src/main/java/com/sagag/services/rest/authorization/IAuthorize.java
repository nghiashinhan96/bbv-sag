package com.sagag.services.rest.authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

/**
 * Interface for verifying permission.
 */
public interface IAuthorize {

  /**
   * Authorizes the target object for a specific user.
   *
   * @param authed the owner of action
   * @param targetDomainObject the target of the action
   * @return <code>true</code> if user has permission, false otherwise
   */
  boolean authorize(Authentication authed, Object targetDomainObject);

  /**
   * Returns the authorize type.
   *
   * @return the string of authorize type
   */
  String authorizeType();

  default boolean support(String type) {
    return StringUtils.equals(authorizeType(), type);
  }

}
