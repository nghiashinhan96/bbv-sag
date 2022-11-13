package com.sagag.services.rest.controller;

import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.AxUserDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;
import com.sagag.eshop.service.sso.SsoLoginProfileService;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to provide APIs for external users such as AX SSO users.
 */
@RestController
@RequestMapping("/external/users")
@Api(tags = "External Users APIs")
public class ExternalUsersController {

  private static final String CREATE_AX_USER_DESC = "Create AX SSO user";
  private static final String CREATE_AX_USER_NOTE =
      "The service will create a new user from AX SSO profile. "
          + "Typically, this user should be assigned the role before continuing eConnect";

  @Autowired
  private UserService userService;

  @Autowired
  private SsoLoginProfileService ssoLoginProfileService;

  @ApiOperation(value = CREATE_AX_USER_DESC, notes = CREATE_AX_USER_NOTE)
  @PostMapping(value = "/create",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Deprecated
  public AxUserDto createAXUser(@RequestBody UserProfileDto userProfileDto) {
    return userService.createAXUser(userProfileDto);
  }

  @ApiOperation(value = CREATE_AX_USER_DESC, notes = CREATE_AX_USER_NOTE)
  @PostMapping(value = "/v2/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public SsoLoginProfileResponseDto createAXUserV2(
      @RequestBody SsoLoginProfileRequestDto requestDto) {
    return ssoLoginProfileService.createProfile(requestDto);
  }
}
