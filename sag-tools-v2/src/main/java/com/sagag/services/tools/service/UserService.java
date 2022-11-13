package com.sagag.services.tools.service;

import java.util.Optional;

/**
 * Interface to define user service APIs.
 */
public interface UserService {

  public static final String AFFILIATE = "affiliate";

  /**
   * Returns the organisation id to which the user belongs.
   *
   * @param userId the user id
   * @return the organisation id that the user belongs.
   */
  Optional<Integer> getOrgIdByUserId(final Long userId);
}
