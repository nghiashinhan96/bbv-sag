package com.sagag.services.rest.controller;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.incentive.authcookie.CookiePrivacyException;
import com.sagag.services.rest.resource.IncentiveLinkResource;
import com.sagag.services.service.api.IncentiveBusinessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Incentive points controller class.
 */
@RestController
@RequestMapping("/incentive")
@Api(tags = { "Incentive Points APIs" })
public class IncentivePointsController {

  @Autowired
  private IncentiveBusinessService incentiveBusService;

  @ApiOperation(value = "Generate happy bonus link to open a new tab",
      notes = "Expired token in 1 minute")
  @GetMapping(value = "/link", produces = MediaType.APPLICATION_JSON_VALUE)
  public IncentiveLinkResource getIncentiveInfo(final OAuth2Authentication authed)
      throws CookiePrivacyException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return new IncentiveLinkResource(incentiveBusService.getIncentiveUrl(user));
  }

  /**
   * Saves accept happy point term.
   *
   * @param authed the authenticated user request
   */
  @ApiOperation(value = "Save happy point term",
      notes = "The service will save user-setting for accept happy point term")
  @PostMapping(value = "/save-term", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void saveAccetpHappyPointTerm(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    incentiveBusService.saveAccetpHappyPointTerm(user.getId());
  }

}
