package com.sagag.services.admin.business.service;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;
import com.sagag.services.domain.eshop.criteria.UserExportRequest;
import com.sagag.services.domain.eshop.dto.BackOfficeUserDto;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;

import java.util.List;

public interface BackOfficeService {

  /**
   * Returns User Setting.
   *
   * @param userId the user id
   * @return the user setting
   * @throws UserValidationException exception when user validation failed.
   */
  public BackOfficeUserDto getBackOfficeSetting(long userId) throws UserValidationException;

  /**
   * Updates User Setting.
   *
   * @param backOfficeUserSettingDto the user setting to update
   * @throws UserValidationException exception when user validation failed.
   */
  public void saveUserSetting(final BackOfficeUserSettingDto backOfficeUserSettingDto)
      throws UserValidationException;

  /**
   * Exports the users info.
   *
   * @param criteria user criteria to search
   * @return a list users
   */
  public List<ExportingUserDto> getExportingUsers(UserExportRequest criteria);
}
