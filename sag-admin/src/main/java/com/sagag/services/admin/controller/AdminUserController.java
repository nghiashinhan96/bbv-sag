package com.sagag.services.admin.controller;

import com.sagag.eshop.service.api.UserManageService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.admin.business.service.BackOfficeService;
import com.sagag.services.admin.exception.UserExportException;
import com.sagag.services.admin.exporter.BackOfficeUserExportCriteria;
import com.sagag.services.admin.exporter.BackOfficeUserExporter;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;
import com.sagag.services.domain.eshop.criteria.UserExportRequest;
import com.sagag.services.domain.eshop.criteria.UserSearchCriteriaRequest;
import com.sagag.services.domain.eshop.dto.BackOfficeUserDto;
import com.sagag.services.domain.eshop.dto.BackOfficeUserProfileDto;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.domain.eshop.dto.SytemAdminChangePasswordDto;
import com.sagag.services.domain.eshop.dto.SytemAdminResetPasswordDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserSearchResultItemDto;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.user.password.change.OnbehalfEshopUserUpdatePasswordHandler;
import com.sagag.services.service.user.password.change.SelfSysAdminEshopUserUpdatePasswordHandler;
import com.sagag.services.service.user.password.reset.SelfSysAdminResetPasswordHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Api(tags = "admin")
public class AdminUserController {

  @Autowired
  private UserManageService userMngService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserBusinessService userBusinessService;

  @Autowired
  private BackOfficeService backOfficeService;

  @Autowired
  private BackOfficeUserExporter backOfficeExporter;

  @Autowired
  private OnbehalfEshopUserUpdatePasswordHandler onbehalfEshopUserUpdatePasswordHandler;

  @Autowired
  private SelfSysAdminEshopUserUpdatePasswordHandler selfSysAdminEshopUserUpdatePasswordHandler;

  @Autowired
  private SelfSysAdminResetPasswordHandler selfSysAdminResetPasswordHandler;

  /**
   * Returns current logging user details.
   *
   * @param authed the authenticated user.
   * @return the full user details with customer information.
   */
  @ApiOperation(value = ApiDesc.User.BO_GET_USER_DETAIL_DESC,
      notes = ApiDesc.User.BO_GET_USER_DETAIL_NOTE)
  @GetMapping(value = "/user/detail", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserInfo viewUserDetail(final OAuth2Authentication authed) {
    return userBusinessService.findCacheUser(Long.parseLong(authed.getPrincipal().toString()),
        StringUtils.EMPTY, StringUtils.EMPTY);
  }

  /**
   * Searches active user by search criteria.
   *
   * @param searchCriteriaRequest
   * @return Page<CustomerSearchResultItemDto>
   */
  @ApiOperation(value = ApiDesc.User.BO_SEARCH_USER_DESC, notes = ApiDesc.User.BO_SEARCH_USER_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<UserSearchResultItemDto> searchActiveUserProfile(
      @RequestBody final UserSearchCriteriaRequest searchCriteriaRequest) {
    return userMngService.searchActiveUserProfile(searchCriteriaRequest);
  }

  /**
   * Updates User password.
   *
   * @param eshopUserLoginDto
   * @throws UserValidationException
   */
  @ApiOperation(value = ApiDesc.User.BO_UPDATE_USER_CREDENTIALS_DESC,
      notes = ApiDesc.User.BO_UPDATE_USER_CREDENTIALS_NOTE)
  @PostMapping(value = "/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void updateUserPasswordByAdmin(@RequestBody final EshopUserLoginDto eshopUserLoginDto)
      throws UserValidationException {
    final UserInfo user = userBusinessService.findCacheUser(eshopUserLoginDto.getId(),
      StringUtils.EMPTY, StringUtils.EMPTY);
    onbehalfEshopUserUpdatePasswordHandler.updatePassword(user, eshopUserLoginDto);
  }

  @ApiOperation(value = ApiDesc.User.BO_UPDATE_SYSTEM_ADMIN_USER_PASS_DESC,
      notes = ApiDesc.User.BO_UPDATE_SYSTEM_ADMIN_USER_PASS_NOTE)
  @PostMapping(value = "/system-admin/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateSystemAdminUserPassword(final OAuth2Authentication authed,
      @RequestBody final SytemAdminChangePasswordDto model) throws UserValidationException {
    final UserInfo user = userBusinessService.findCacheUser(
        Long.parseLong(authed.getPrincipal().toString()), StringUtils.EMPTY, StringUtils.EMPTY);
    selfSysAdminEshopUserUpdatePasswordHandler.updatePassword(user, model);
    return ResponseEntity.ok("Update password successfully");
  }

  @ApiOperation(value = ApiDesc.User.BO_RESET_SYSTEM_ADMIN_USER_PASS_DESC,
      notes = ApiDesc.User.BO_RESET_SYSTEM_ADMIN_USER_PASS_NOTE)
  @PostMapping(value = "/system-admin/password/reset", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> resetSystemAdminUserPassword(
      @RequestBody final SytemAdminResetPasswordDto dto) throws UserValidationException {
    selfSysAdminResetPasswordHandler.handle(dto);
    return ResponseEntity.ok("Reset password successfully");
  }

  /**
   * Deactives user login by id to prevent user can login to e-Connect system.
   *
   * @param id the user id to delete
   * @throws UserValidationException
   */
  @ApiOperation(value = ApiDesc.User.BO_DELETE_USER_DESC, notes = ApiDesc.User.BO_DELETE_USER_NOTE)
  @GetMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteUser(@PathVariable("id") final Long id) throws UserValidationException {
    userBusinessService.deactiveUserById(id);
  }

  /**
   * Deletes Dvse and external user by id if affiliate is belongs to Austria.
   *
   * @param id the user id to delete
   * @throws UserValidationException
   * @throws MdmCustomerNotFoundException
   */
  @ApiOperation(value = ApiDesc.User.BO_DELETE_DVSE_EXTERNAL_USER_DESC,
      notes = ApiDesc.User.BO_DELETE_DVSE_EXTERNAL_USER_NOTE)
  @GetMapping(value = "/dvse/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteDvseExternalUser(@PathVariable("id") final Long id)
      throws UserValidationException, MdmCustomerNotFoundException {
    userBusinessService.deleteDvseExternalUserById(id);
  }

  /**
   * Gets User setting && information.
   *
   * @param userId the user id
   * @throws UserValidationException
   */
  @ApiOperation(value = ApiDesc.User.BO_GET_USER_SETTING_DESC,
      notes = ApiDesc.User.BO_GET_USER_SETTING_NOTE)
  @GetMapping(value = "/{id}/settings", produces = MediaType.APPLICATION_JSON_VALUE)
  public BackOfficeUserDto getUserSettings(@PathVariable("id") final long userId)
      throws UserValidationException {
    return backOfficeService.getBackOfficeSetting(userId);
  }

  /**
   * Gets User setting && information.
   *
   * @param backOfficeUserSettingDto the user settings
   * @throws UserValidationException
   */
  @ApiOperation(value = ApiDesc.User.BO_UPDATE_USER_SETTING_DESC,
      notes = ApiDesc.User.BO_UPDATE_USER_SETTING_NOTE)
  @PostMapping(value = "/settings/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void updateUserSettings(
      @RequestBody final BackOfficeUserSettingDto backOfficeUserSettingDto)
      throws UserValidationException {
    backOfficeService.saveUserSetting(backOfficeUserSettingDto);
    userBusinessService.clearCacheUser(Long.valueOf(backOfficeUserSettingDto.getUserId()));
  }

  @ApiOperation(value = ApiDesc.User.EXPORT_EXCEL_DESC, notes = ApiDesc.User.EXPORT_EXCEL_NOTE)
  @PostMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<byte[]> exportUsersExcel(@RequestBody final UserExportRequest request)
      throws UserExportException {
    final List<ExportingUserDto> users =
        this.backOfficeService.getExportingUsers(request);
    final ExportStreamedResult result =
        backOfficeExporter.exportExcel(new BackOfficeUserExportCriteria(users));
    return result.buildResponseEntity();
  }

  @ApiOperation(value = ApiDesc.User.BO_RETRIEVE_USER_API_DESC,
      notes = ApiDesc.User.BO_RETRIEVE_USER_API_NOTE)
  @GetMapping(value = "/profile/new")
  public UserProfileDto getNewProfile() {
    return userService.getUserProfileTemplate();
  }

  @ApiOperation(value = ApiDesc.User.BO_CREATE_USER_API_DESC,
      notes = ApiDesc.User.BO_CREATE_USER_API_NOTE)
  @PostMapping(value = "/profile/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public void createUser(@RequestBody final BackOfficeUserProfileDto userInfo)
      throws ValidationException, MdmCustomerNotFoundException {
    userBusinessService.createUserBySystemAdmin(userInfo);
  }
}
