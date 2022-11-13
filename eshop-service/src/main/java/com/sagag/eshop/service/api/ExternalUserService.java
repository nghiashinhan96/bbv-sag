package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExternalUserService {

  /**
   * Returns the external user with DVSE app.
   *
   * @return the optional DVSE external user.
   */
  Optional<ExternalUserDto> getDvseExternalUserByUserId(Long userId);

  ExternalUser addExternalUser(ExternalUserDto externalUserDto);

  Optional<ExternalUserDto> getExternalUser(String username, String password, ExternalApp app);

  Page<ExternalUserDto> searchInactiveExternalUser(ExternalApp app, Pageable pageable);

  List<ExternalUserDto> updateExternalUsers(List<ExternalUserDto> externalUsers);

  /**
   * Returns the external user by unique username per app.
   *
   * @param username the username which is unique per app
   * @param app the external app
   * @return the {@link ExternalUser}
   */
  Optional<ExternalUser> searchByUsernameAndApp(String username, ExternalApp app);

  /**
   * Removes the external user by id.
   *
   * @param id the external user id
   */
  void removeExternalUserById(Long id);

  /**
   * Checks if external externalUsername existed .
   *
   * @param externalUsername the externalUsername to check
   * @return <code>true</code> if externalUsername existed
   */
  boolean isUsernameExisted(String externalUsername);

  /**
   * Counts virtual user pool for specific customer.
   *
   * @param orgCode
   * @return number of existing
   */
  int countVirtualUserExisted(String orgCode);

  /**
   * Gets first available virtual user by orgCode.
   *
   * @param customerNumber the customerNumber to check
   * @return first available ExternalUserDto
   */
  Optional<ExternalUserDto> getAvailableVirtualUser(String customerNumber);

  /**
   * Releases lock virtual dvse user.
   *
   * @param eshopUserIds the Eshop virtual user need to be released
   */
  void releaseVirtualUsers(List<Long> eshopUserIds);

  /**
   * Returns the list of external accounts of user by user id.
   *
   * @param userId the selected user id
   * @return the list of external user accounts.
   */
  List<ExternalUserDto> searchExternalUsersByUserId(Long userId);
}
