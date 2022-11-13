package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopUser;

/**
 * Interface to define user service APIs.
 */
public interface AutonetUserService {

  /**
   * @param username the username from Autonet
   * @param langIso the login language
   * @param affiliate the affiliate
   * @return {@link EshopUser}
   */
  EshopUser createAutonetUser(String username, String langIso, String affiliate);
}
