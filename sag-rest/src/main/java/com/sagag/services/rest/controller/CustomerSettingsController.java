package com.sagag.services.rest.controller;

import com.sagag.eshop.service.api.CustomerSettingsService;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.CustomerSettingsDto;
import com.sagag.services.hazelcast.api.CustomerCacheService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer settings controller class.
 */
@RestController
@RequestMapping("/customer/settings")
@Api(tags = "Customer Settings APIs")
public class CustomerSettingsController {

  @Autowired
  private CustomerSettingsService customerSettingsService;

  @Autowired
  private CustomerCacheService customerCacheService;

  @ApiOperation(value = "Get current customer settings information",
      notes = "The service will get customer settings information")
  @GetMapping(value = "/default/", produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerSettingsDto getCustomerSettings(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return customerSettingsService.getCustomerSetting(user.getId());
  }

  @ApiOperation(value = "Update customer settings information",
      notes = "The service will update customer settings information")
  @PutMapping(value = "/default/edit",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerSettingsDto editDefaultSettings(final OAuth2Authentication authed,
      @RequestBody final CustomerSettingsDto customerSettingsDto) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    customerCacheService.clearCache(user.getCustNrStr());
    return customerSettingsService.updateCustomerSetting(user.getId(), customerSettingsDto);
  }

}
