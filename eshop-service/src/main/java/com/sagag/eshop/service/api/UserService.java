package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.dto.AxUserDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.UserManagementDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface to define user service APIs.
 */
public interface UserService {

  /**
   * Returns {@link EshopUser} by id.
   *
   * @param id the id to search user.
   * @return the null-able user.
   */
  EshopUser getUserById(Long id);

  /**
   * Returns {@link EshopUser} by email.
   *
   * @param email the email to search user.
   * @return the null-able user.
   */
  List<EshopUser> getUsersByEmail(String email);

  /**
   * Returns Returns a list of {@link EshopUser} who match with the username.
   *
   * @param username the username to search users.
   * @return user list.
   */
  List<EshopUser> getUsersByUsername(String username);

  /**
   * Creates eshop user for the current logging user
   *
   * @param userProfile user's information for creating
   * @param user the current logging user
   * @param genPassword
   * @return EshopUser
   */
  EshopUser createUser(UserProfileDto userProfile, UserInfo user, String genPassword)
      throws ValidationException;

  /**
   * Creates eshop user for another customer
   *
   * @param userProfile    user's information for creating
   * @param user           the current logging user
   * @param genPassword
   * @param targetCustomer the customer of user will belong to
   * @return EshopUser
   * @throws ValidationException
   */
  EshopUser createUserForOtherCustomer(UserProfileDto userProfile, UserInfo user, String genPassword,
      OrganisationDto targetCustomer) throws ValidationException;

  /**
   * Get user profile template.
   *
   * @return {@link UserProfileDto}
   */
  UserProfileDto getUserProfileTemplate();

  /**
   * Returns {@link UserManagementDto} by userId.
   *
   * @param user the current user
   * @return the null-able userDto.
   */
  List<UserManagementDto> getUserSameOrganisation(UserInfo user);

  /**
   * Deactives user by id.
   *
   * @param userId the id of user which need to delete
   */
  void deactiveUserById(Long userId);

  /**
   * Gets payment setting of current user.
   *
   * @param isSalesOnbehalfUser the flag user is login onbehalf or not
   * @param isWholesaler the flag to determine wholesaler
   * @return {@link PaymentSettingDto}
   */
  PaymentSettingDto getPaymentSetting(boolean isSalesOnbehalfUser, boolean isWholesaler);

  /**
   * Returns the 'on behalf' customer admin for sales.
   *
   * @param customerNr the customer number.
   * @return the admin username.
   */
  String searchCustomerAdminUser(final String customerNr);

  /**
   * Creates the AX SSO user who is typically the Sales guys. However, we can change the role by
   * Admin function.
   *
   * @param userProfileDto the creating user profile.
   * @return the created {@link AxUserDto}.
   * @deprecated replaced by {@link #createUser(userProfileDto, userlogin, generated password }
   */
  @Deprecated
  AxUserDto createAXUser(UserProfileDto userProfileDto);

  /**
   * Returns full name which combines the first and last name of the user.
   *
   * @param userId the user id
   * @return full name of the user
   */
  String getFullName(Long userId);

  /**
   * Save eshop user.
   *
   * @param eshopUser eshopUser entity
   */
  void saveEshopUser(final EshopUser eshopUser);

  /**
   * Returns the session user info.
   *
   * @param currentLoggedUserId the current user id
   * @return the userInfo
   */
  UserInfo getSessionUserInfo(long currentLoggedUserId);

  /**
   * Returns the organisation id to which the user belongs.
   *
   * @param userId the user id
   * @return the organisation id that the user belongs.
   */
  Optional<Integer> getOrgIdByUserId(final Long userId);

  /**
   * Returns all usernames that belong the same organisation.
   *
   * @param orgId the id of organisation
   * @return a list of usernames
   */
  List<String> getAllUsernamesByOrgId(Integer orgId);

  /**
   * Returns username by <code>userId</code>.
   *
   * @param userId the user id to get its username
   * @return the username
   */
  String getUsernameById(Long userId);

  /**
   * Returns affiliate short name that the user belong to.
   *
   * @param userId id of user need to get affiliate shortName.
   * @return the affiliate short name.
   */
  Optional<String> findAffiliateShortNameById(Long userId);

  /**
   * Updates latest login date.
   *
   * @param userId user id who logged in
   * @param signInDate the signInDate
   */
  void updateLoginSignInDate(long userId, Date signInDate);

  /**
   * Creates eshop user admin by customer.
   *
   */
  EshopUser createEshopUserAdminByCustomer(UserProfileDto userProfile, String rawPassword,
      Optional<UserType> userType, Organisation customer, SupportedAffiliate affiliate,
      String langISO);

  /**
   * Creates virtual user from user id.
   *
   * @param id the original user id
   * @param userType the user type
   * @param langIso the login language
   * @return the EshopUser
   */
  EshopUser createVirtualUser(Long id, UserType userType, String langIso);

  /**
   * Clones eshopUser. Cloned object should have different Id, different settingId
   *
   * @param eshopUser
   * @return EshopUser
   */
  EshopUser clone(EshopUser eshopUser);

  /**
   * Checks if the user has a specific permission.
   *
   * @param userId the user id
   * @param perm the perm
   * @return <code> true</code> if the user has the permission. Otherwise, return <code>false</code>
   *         .
   */
  boolean hasPermission(long userId, PermissionEnum perm);

  /**
   * @param user the user to update language if language is changed
   * @param langIso the login language
   * @return {@link EshopUser}
   */
  EshopUser updateUserLanguage(EshopUser user, String langIso);

  boolean hasRoleByUsername(String username, EshopAuthority role);

  /**
   * Create eShop admin user for APM customer
   *
   * @param userProfile
   * @param hashPassword
   * @param salt
   * @param userType
   * @param customer
   * @param affiliate
   * @param langISO
   * @return eShop user {@link EshopUser}
   */
  EshopUser createEshopUserAdminForAPMCustomer(UserProfileDto userProfile, String hashPassword,
      String salt, Optional<UserType> userType, Organisation customer, SupportedAffiliate affiliate,
      String langISO);
}
