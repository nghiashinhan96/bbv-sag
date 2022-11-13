package com.sagag.services.rest.controller.finalcustomer;

import com.sagag.eshop.repo.criteria.finaluser.FinalUserSearchCriteria;
import com.sagag.eshop.service.api.FinalCustomerUserService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.converter.OrganisationConverters;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerUserDto;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.OrganisationDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.rest.authorization.annotation.HasWholesalerPreAuthorization;
import com.sagag.services.rest.authorization.annotation.IsTheSameWholesalerPreAuthorization;
import com.sagag.services.service.api.UserBusinessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/final-customer-user", produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Final Customer User APIs")
public class FinalCustomerUserController {

  @Autowired
  private UserBusinessService userBusinessService;

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private FinalCustomerUserService finalUserService;

  @ApiOperation(value = "The API to create final user for final customer by wholesaler")
  @PostMapping(value = "/{finalCustomerOrgId}/create")
  @ResponseStatus(value = HttpStatus.OK)
  @HasWholesalerPreAuthorization
  public void createUserByWholesaler(@RequestBody UserProfileDto userProfileDto,
      @PathVariable Integer finalCustomerOrgId,
      final OAuth2Authentication authed) throws ValidationException, MdmCustomerNotFoundException {
    final UserInfo userInfo = (UserInfo) authed.getPrincipal();
    userProfileDto
        .setAccessUrl(userInfo.getSettings().getAffiliateEhPortalUrl() + Customer.SUB_URL_FORGOT_PWORD_VERIFYCODE);
    OrganisationDto org = orgService.getByOrgId(finalCustomerOrgId)
        .map(OrganisationConverters.organisationConverter())
        .orElseThrow(() -> new IllegalArgumentException("final Customer not found"));
    userBusinessService.createUserByAdmin(userInfo, userProfileDto, org);
  }

  @ApiOperation(value = "The API to get get user profile by wholesaler")
  @GetMapping(value = "/{userId}/profile")
  @IsTheSameWholesalerPreAuthorization
  public UserProfileDto viewOtherUserProfile(@PathVariable("userId") final Long userId) {
    return finalUserService.getUserProfile(userId);
  }

  @ApiOperation(value = "The API to update user for final customer by wholesaler")
  @PostMapping(value = "/{userId}/update")
  @ResponseStatus(value = HttpStatus.OK)
  @IsTheSameWholesalerPreAuthorization
  public void updateUserInformation(@RequestBody UserProfileDto userProfileDto,
      @PathVariable("userId") final Long userId,
      final OAuth2Authentication authed) throws ValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    userProfileDto.setId(userId);
    finalUserService.updateUserProfile(userProfileDto, user, true);
    userBusinessService.clearCacheUser(userProfileDto.getId());
  }

  @ApiOperation(value = "The API to get own final user profile")
  @GetMapping(value = "/profile")
  public UserProfileDto viewUserProfile(final OAuth2Authentication authed,
      @RequestParam(value = "finalCustomerId", required = false) Integer finalCustomerId) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    if (user.isFinalUserRole()) {
      finalCustomerId = user.getFinalCustomerOrgId();
    }
    return finalUserService.getUserProfile(user, finalCustomerId);
  }

  @ApiOperation(value = "The API to create final user")
  @PostMapping(value = "/create")
  @ResponseStatus(value = HttpStatus.OK)
  public void createUser(@RequestBody UserProfileDto userProfileDto,
      final OAuth2Authentication authed) throws ValidationException, MdmCustomerNotFoundException {
    final UserInfo userInfo = (UserInfo) authed.getPrincipal();
    userProfileDto
        .setAccessUrl(userInfo.getSettings().getAffiliateEhPortalUrl() + Customer.SUB_URL_FORGOT_PWORD_VERIFYCODE);
    userBusinessService.createUserByAdmin(userInfo, userProfileDto, userInfo.getFinalCustomer());
  }

  @ApiOperation(value = "The API to delete final user")
  @PostMapping(value = "/{userId}/profile/delete")
  @ResponseStatus(value = HttpStatus.OK)
  @IsTheSameWholesalerPreAuthorization
  public void deleteUser(@PathVariable("userId") final Long userId) throws UserValidationException {
    userBusinessService.deactiveUserById(userId);
  }

  @ApiOperation(
      value = "The API to search final user list for final customer selected from wholesaler.")
  @PostMapping("/{finalCustomerOrgId}/search")
  @HasWholesalerPreAuthorization
  public Page<FinalCustomerUserDto> searchFinalUsersByCriteria(OAuth2Authentication authed,
      @RequestBody FinalUserSearchCriteria criteria,
      @PathVariable("finalCustomerOrgId") Integer finalCustomerOrgId,
      @PageableDefault Pageable pageable) throws ResultNotFoundException {
    final Page<FinalCustomerUserDto> finalUsers = finalUserService
        .searchFinalUsersBelongToFinalCustomer(finalCustomerOrgId, criteria);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(finalUsers);
  }

  @ApiOperation(value = "The API to search final user belong to current final customer.")
  @PostMapping("/search")
  public Page<FinalCustomerUserDto> searchFinalUsersByCriteria(OAuth2Authentication authed,
      @RequestBody FinalUserSearchCriteria criteria) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Page<FinalCustomerUserDto> finalUsers = finalUserService
        .searchFinalUsersBelongToFinalCustomer(user.getFinalCustomerOrgId(), criteria);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(finalUsers);
  }
}
