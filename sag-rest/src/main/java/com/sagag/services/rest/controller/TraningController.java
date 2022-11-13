package com.sagag.services.rest.controller;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.incentive.api.TrainingSingleSignOnService;
import com.sagag.services.incentive.domain.TrainingLoginDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training")
@Api(tags = { "Traning APIs" })
public class TraningController {

  private static final String OUTLET_USER_ID = "user_outlet";
  private static final String OUTLET_USER_FIRST_NAME = "Outlet";
  private static final String OUTLET_USER_LAST_NAME = "SAG";
  private static final long OUTLET_CUSTOMER_NUMBER = 0l;


  @Autowired
  private TrainingSingleSignOnService traningSingleSignOnService;

  @ApiOperation(value = "Get traning sso information")
  @GetMapping(value = "/sso", produces = MediaType.APPLICATION_JSON_VALUE)
  public TrainingLoginDto getTraningAuthenInfo(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return traningSingleSignOnService.getAuthenInfo(user.getSupportedAffiliate(),
        Long.valueOf(user.getCustNrStr()), String.valueOf(user.getId()), user.getFirstName(),
        user.getLastName());
  }

  @ApiOperation(value = "Get outlet sso information")
  @GetMapping(value = "/sso/outlet", produces = MediaType.APPLICATION_JSON_VALUE)
  public TrainingLoginDto getOutletAuthenInfo(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return traningSingleSignOnService.getAuthenInfo(user.getSupportedAffiliate(),
        OUTLET_CUSTOMER_NUMBER, OUTLET_USER_ID, OUTLET_USER_FIRST_NAME,
        OUTLET_USER_LAST_NAME);
  }
}
