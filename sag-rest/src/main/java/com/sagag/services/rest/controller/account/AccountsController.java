package com.sagag.services.rest.controller.account;

import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.api.UserCreateService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;
import com.sagag.services.domain.eshop.dto.SettingUpdateDto;
import com.sagag.services.domain.eshop.dto.UserManagementDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserSettingsDto;
import com.sagag.services.domain.sag.external.Employee;
import com.sagag.services.hazelcast.api.UserCacheService;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.rest.resource.EmployeeResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.request.UserPaymentSettingRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * System user controller class.
 */
@RestController
@Api(tags = "Users Accounts APIs")
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountsController {

  private static final String DELETE_DVSE_USER_URI = "/users/profile/delete-dvse-user";
  private static final String DELETE_USER_URI = "/users/profile/delete-user";

  @Autowired
  private EmployeeExternalService employeeExtService;
  @Autowired
  private UserService userService;
  @Autowired
  private UserCreateService userCreateService;
  @Autowired
  private UserBusinessService userBusinessService;
  @Autowired
  private UserSettingsService userSettingsService;
  @Autowired
  private UserCacheService userCacheService;
  @Autowired
  private LocaleContextHelper localeContextHelper;

  /**
   * Returns current logging user details.
   *
   * @param authed the authenticated user.
   * @return the full user details with customer information.
   */
  @GetMapping(value = "/user/detail")
  public UserInfo viewUserDetail(HttpServletRequest request, final OAuth2Authentication authed) {
    return (UserInfo) authed.getPrincipal();
  }

  @ApiOperation(value = ApiDesc.UserAccounts.VIEW_PROFILE_DESC,
      notes = ApiDesc.UserAccounts.VIEW_PROFILE_NOTE)
  @GetMapping(value = "/user/profile/")
  public UserProfileDto viewProfile(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userCreateService.getUserProfile(user, false);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.VIEW_USER_PROFILE_DESC,
      notes = ApiDesc.UserAccounts.VIEW_USER_PROFILE_NOTE)
  @GetMapping(value = "/users/{id}/profile/")
  public UserProfileDto viewUserProfile(final OAuth2Authentication authentication,
      @PathVariable("id") final Long userId) {
    return userCreateService.getUserProfile(userId, true);
  }

  /**
   * Update my own profile.
   *
   * @param userProfileDto updated information
   * @param authed the authenticated user
   * @throws ValidationException
   */
  @ApiOperation(value = ApiDesc.UserAccounts.PROFILE_UPDATE_INFORMATION_NOTE,
      notes = ApiDesc.UserAccounts.PROFILE_UPDATE_INFORMATION_DESC)
  @PostMapping(value = "/user/profile/update")
  @ResponseStatus(value = HttpStatus.OK)
  @PreAuthorize("hasPermission(#userProfileDto.id,'isTheSameUser')")
  public void updateInformation(@RequestBody UserProfileDto userProfileDto,
      final OAuth2Authentication authed) throws ValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    userCreateService.updateUserProfile(userProfileDto, user, false);
    LocaleContextHolder.setLocale(localeContextHelper.toLocale(user.getLanguage()));
    userBusinessService.clearCacheUser(userProfileDto.getId());
  }

  /**
   * Update other profile in its company.
   *
   * @param userProfileDto updated information
   * @param authed my certificate
   * @throws ValidationException
   */
  @ApiOperation(value = ApiDesc.UserAccounts.PROFILE_UPDATE_USER_INFORMATION_NOTE,
      notes = ApiDesc.UserAccounts.PROFILE_UPDATE_USER_INFORMATION_DESC)
  @PostMapping(value = "/users/profile/update")
  @ResponseStatus(value = HttpStatus.OK)
  @PreAuthorize("hasPermission(#userProfileDto.id,'isTheSameCustomer')")
  public void updateUserInformation(@RequestBody UserProfileDto userProfileDto,
      final OAuth2Authentication authed) throws ValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    userCreateService.updateUserProfile(userProfileDto, user, true);
    userBusinessService.clearCacheUser(userProfileDto.getId());
  }

  @ApiOperation(value = ApiDesc.UserAccounts.GET_ALL_USER_SAME_ORGANISATION_DESC,
      notes = ApiDesc.UserAccounts.GET_ALL_USER_SAME_ORGANISATION_NOTE)
  @GetMapping(value = "/users/all-user")
  public List<UserManagementDto> getUserSameOrganisation(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userService.getUserSameOrganisation(user);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.DELETE_USER_DESC,
      notes = ApiDesc.UserAccounts.DELETE_USER_NOTE)
  @PostMapping(value = DELETE_USER_URI)
  @ResponseStatus(value = HttpStatus.OK)
  @PreAuthorize("hasPermission(#userId,'isTheSameCustomer')")
  public void deleteUser(@RequestBody final Long userId) throws UserValidationException {
    userBusinessService.deactiveUserById(userId);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.DELETE_DVSE_USER_DESC,
      notes = ApiDesc.UserAccounts.DELETE_DVSE_USER_NOTE)
  @PostMapping(value = DELETE_DVSE_USER_URI)
  @ResponseStatus(value = HttpStatus.OK)
  @PreAuthorize("hasPermission(#userId,'isTheSameCustomer')")
  public void deleteDvseUser(@RequestBody final Long userId)
      throws UserValidationException, MdmCustomerNotFoundException {
    userBusinessService.deleteDvseExternalUserById(userId);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.GET_SETTINGS_DESC,
      notes = ApiDesc.UserAccounts.GET_SETTINGS_NOTE)
  @PostMapping(value = "user/settings", produces = MediaType.APPLICATION_JSON_VALUE)
  public UserSettingsDto getUserSettings(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userBusinessService.getUserSettings(user);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.GET_PAYMENT_SETTINGS_DESC,
      notes = ApiDesc.UserAccounts.GET_PAYMENT_SETTINGS_NOTE)
  @PostMapping(value = "user/payment", produces = MediaType.APPLICATION_JSON_VALUE)
  public PaymentSettingDto getPaymentSettings(final OAuth2Authentication authed,
      @RequestBody final UserPaymentSettingRequest request) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userBusinessService.filterUserPaymentSetting(user);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.PROFILE_UPDATE_PAYMENT_DESC,
      notes = ApiDesc.UserAccounts.PROFILE_UPDATE_PAYMENT_NOTE)
  @PostMapping(value = "/user/settings/update")
  @ResponseStatus(value = HttpStatus.OK)
  @PreAuthorize("hasPermission(#settingsDto.userId,'isTheSameUser')")
  public UserSettingsDto updateSetting(@RequestBody SettingUpdateDto settingsDto,
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userBusinessService.updateMyUserSettings(settingsDto, user);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.UPDATE_USER_PRICE_SETTING_DESC,
      notes = ApiDesc.UserAccounts.UPDATE_USER_PRICE_SETTING_NOTE)
  @PostMapping(value = "/user/view-net-price/toggle")
  @ResponseStatus(value = HttpStatus.OK)
  public void toggle(final OAuth2Authentication auth) {
    final UserInfo userInfo = (UserInfo) auth.getPrincipal();
    final long userId = userInfo.getId();
    final UserSettings userSettings = userSettingsService.getSettingsByUserId(userId);
    Assert.assertTrue(userSettings.isNetPriceView());
    userSettings.setCurrentStateNetPriceView(!userSettings.isCurrentStateNetPriceView());
    UserSettings updateUserSettings = userSettingsService.updateUserSettings(userSettings);
    userInfo.getSettings().setUserSettings(updateUserSettings);
    userCacheService.put(userInfo);
  }

  @ApiOperation(value = ApiDesc.UserAccounts.PROFILE_UPDATE_USER_PAYMENT_DESC,
      notes = ApiDesc.UserAccounts.PROFILE_UPDATE_USER_PAYMENT_NOTE)
  @PostMapping(value = "/users/settings/update")
  @ResponseStatus(value = HttpStatus.OK)
  @PreAuthorize("hasPermission(#settingsDto.userId,'isTheSameCustomer')")
  public void updatePaymentSettingById(@RequestBody SettingUpdateDto settingsDto,
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    userBusinessService.updateUserSettingsByAdmin(settingsDto, user.getOrganisationId(),
        userBusinessService.getUserRoleName(settingsDto.getUserId()));
  }

  @ApiOperation(value = ApiDesc.UserAccounts.GET_USER_SETTINGS_DESC,
      notes = ApiDesc.UserAccounts.GET_USER_SETTINGS_NOTE)
  @GetMapping(value = "/users/{id}/settings")
  public UserSettingsDto getUserSettings(@PathVariable("id") final String id,
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userBusinessService.getUserSettingsByAdmin(user, Long.parseLong(id));
  }

  @ApiOperation(value = ApiDesc.UserAccounts.GET_USER_PAYMENT_SETTINGS_DESC,
      notes = ApiDesc.UserAccounts.GET_USER_PAYMENT_SETTINGS_NOTE)
  @GetMapping(value = "/users/{id}/payment")
  public PaymentSettingDto getUserPaymentSettings(@PathVariable("id") final String id,
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return userBusinessService.getUserPaymentSettingByAdmin(user, Long.parseLong(id));
  }

  @ApiOperation(value = ApiDesc.UserAccounts.GET_EMPLOYEE_INFO_DESC,
      notes = ApiDesc.UserAccounts.GET_EMPLOYEE_INFO_NOTE)
  @GetMapping(value = "/user/employee")
  public EmployeeResource getSalesEmployeeInfo(final OAuth2Authentication authed,
      @RequestParam(name = "affiliate") final String affiliate,
      @RequestParam(name = "emailAddress") final String emailAddress) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final SupportedAffiliate aff = SupportedAffiliate.fromDesc(affiliate);
    final String employeeNrOrEmailAddress;
    if (aff.isSbAffiliate()) {
      final String employeeNr = user.getSalesEmployeeNumber();
      log.debug("Using employee number = {} with Serbia country and WINT ERP", employeeNr);
      employeeNrOrEmailAddress = employeeNr;
    } else {
      employeeNrOrEmailAddress = emailAddress;
    }
    final Optional<Employee> employeeOpt = employeeExtService
      .findEmployee(aff.getCompanyName(), employeeNrOrEmailAddress);

    final EmployeeResource resource = new EmployeeResource();
    employeeOpt.ifPresent(resource::setEmployee);
    resource.add(linkTo(methodOn(AccountsController.class)
      .getSalesEmployeeInfo(authed, affiliate, emailAddress)).withSelfRel());
    return resource;
  }

  /**
   * Creates new user by customer administrator.
   *
   * @param userProfileDto user information to create
   * @param authed the authenticated user
   * @throws ValidationException thrown when the validation failed
   * @throws MdmCustomerNotFoundException thrown when customer not found
   */
  @ApiOperation(value = ApiDesc.UserAccounts.CREATE_USER_DESC,
      notes = ApiDesc.UserAccounts.CREATE_USER_NOTE)
  @PostMapping(value = "/customer/users/create")
  @ResponseStatus(value = HttpStatus.OK)
  public void createUser(@RequestBody UserProfileDto userProfileDto,
      final OAuth2Authentication authed) throws ValidationException, MdmCustomerNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    userBusinessService.createUserByAdmin(user, userProfileDto);
  }
}
