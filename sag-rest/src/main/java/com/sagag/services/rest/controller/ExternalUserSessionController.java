package com.sagag.services.rest.controller;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.sag.external.ExternalUserSession;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.user.handler.ExternalUserSessionContextHandler;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/autonet" }, produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "External User Session")
@AutonetProfile
public class ExternalUserSessionController {

  @Autowired
  private ExternalUserSessionContextHandler extUserContextHandler;

  /**
   * Saves information of initiate connect session into UserInfo.
   *
   * @param request AutonetSession request
   * @param authed OAuth2Authentication
   */
  @PostMapping(value = "/save-session", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = ApiDesc.ExternalUserSession.GET_INITIATE_SESSION,
      notes = ApiDesc.ExternalUserSession.GET_INITIATE_SESSION_NOTE)
  public void initiateSession(@RequestBody ExternalUserSession request, OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    extUserContextHandler.saveExternalUserSessionIntoUserContext(user.getId(), request);
  }

}
