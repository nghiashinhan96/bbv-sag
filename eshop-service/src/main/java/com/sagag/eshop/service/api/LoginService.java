package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Login;

import java.util.Date;

/**
 * Login service interface to define the APIs for login.
 */
public interface LoginService {

  void update(Login login);

  /**
   * Updates last sales on behalf of date.
   *
   * @param id the login id
   * @param lastOnBehalfOfDate the date of logging in
   */
  void updateLastOnBehalfOfDate(int id, Date lastOnBehalfOfDate);

  /**
   * Updates first sign in date.
   *
   * @param id the login id
   * @param firstLoginDate the date of logging in
   */
  void updateFirstLoginDate(int id, Date firstLoginDate);

  /**
   * Returns Login information for specific user.
   *
   * @param userId the user identification
   * @return the login information
   */
  Login getLoginForUser(long userId);

  /**
   * Inserts Login.
   *
   * @param eshopUser
   * @param rawPassword
   * @param affiliateShortName
   * @return created Login entity
   */
  Login createLogin(EshopUser eshopUser, String rawPassword);

  /**
   * Inserts Login.
   *
   * @param eshopUser
   * @param passwordHash
   * @param passwordSalt
   * @return created Login entity
   */
  Login createAPMUserLogin(EshopUser eshopUser, String passwordHash, String passwordSalt);
}
