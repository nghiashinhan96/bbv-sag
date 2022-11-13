package com.sagag.services.rest.controller.account;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.domain.eshop.dto.EshopUserLoginDto;
import com.sagag.services.rest.authorization.annotation.IsTheSameWholesalerPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.user.password.change.OnbehalfEshopUserUpdatePasswordHandler;
import com.sagag.services.service.user.password.change.SelfEshopUserUpdatePasswordHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to define service APIs for change password.
 */
@RestController
@Api(tags = "Change Password APIs")
public class ChangePasswordController {

  @Autowired
  private SelfEshopUserUpdatePasswordHandler selfEshopUserUpdatePasswordHandler;

  @Autowired
  private OnbehalfEshopUserUpdatePasswordHandler onbehalfEshopUserUpdatePasswordHandler;

  @ApiOperation(value = ApiDesc.ChangePassword.PROFILE_UPDATE_PW_DESC,
      notes = ApiDesc.ChangePassword.PROFILE_UPDATE_PW_NOTE)
  @PostMapping(value = "/user/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasPermission(#eshopUserLoginDto.id,'isTheSameUser')")
  public void updateUserPassword(final OAuth2Authentication authed,
      @RequestBody final EshopUserLoginDto eshopUserLoginDto) throws UserValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    selfEshopUserUpdatePasswordHandler.updatePassword(user, eshopUserLoginDto);
  }

  @ApiOperation(value = ApiDesc.ChangePassword.PROFILE_UPDATE_USER_PW_DESC,
      notes = ApiDesc.ChangePassword.PROFILE_UPDATE_USER_PW_NOTE)
  @PostMapping(value = "/users/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasPermission(#eshopUserLoginDto.id,'isTheSameCustomer')")
  public void updateUserPasswordByAdmin(final OAuth2Authentication authed,
      @RequestBody final EshopUserLoginDto eshopUserLoginDto) throws UserValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    onbehalfEshopUserUpdatePasswordHandler.updatePassword(user, eshopUserLoginDto);
  }

  @ApiOperation(value = "The API to update password of final user")
  @PostMapping(value = "/final-customer-user/{userId}/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @IsTheSameWholesalerPreAuthorization
  public void updateFinalCustomerUserPasswordByAdmin(final OAuth2Authentication authed,
      @PathVariable("userId") final Long userId,
      @RequestBody final EshopUserLoginDto eshopUserLoginDto) throws UserValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    eshopUserLoginDto.setId(userId);
    eshopUserLoginDto.setRedirectUrl(user.getSettings().getAffiliateEhPortalUrl());
    eshopUserLoginDto.setFinalUser(true);
    onbehalfEshopUserUpdatePasswordHandler.updatePassword(user, eshopUserLoginDto);
  }

  @ApiOperation(value = "The API to self update password")
  @PostMapping(value = "/final-customer-user/password/update", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasPermission(#eshopUserLoginDto.id, 'isTheSameWholesaler')")
  public void updateFinalCustomerUserPassword(final OAuth2Authentication authed,
      @RequestBody final EshopUserLoginDto eshopUserLoginDto)
      throws UserValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    eshopUserLoginDto.setRedirectUrl(user.getSettings().getAffiliateEhPortalUrl());
    eshopUserLoginDto.setFinalUser(true);
    selfEshopUserUpdatePasswordHandler.updatePassword(user, eshopUserLoginDto);
  }

}
