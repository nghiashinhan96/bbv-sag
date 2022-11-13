package com.sagag.services.rest.controller.anonymous;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.service.api.CustomerBusinessService;

import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/custom/settings")
@PreAuthorize("hasPermission(#hashPass, 'isSecretKey')")
@Api(tags = "Custom Anonymous Settings APIs")
public class CustomSettingsController {

  @Autowired
  private CustomerBusinessService customerBusService;

  @PostMapping(value = "/customers/import", produces = MediaType.APPLICATION_JSON_VALUE)
  public void importCustomersByList(@RequestParam("key") final String hashPass,
      @RequestParam(defaultValue = "postman") final String source,
      @RequestBody final List<UserRegistrationDto> registrationList) throws ServiceException {

    for (UserRegistrationDto registrationItem : registrationList) {
      customerBusService.registerAPMCustomer(registrationItem, StringUtils.EMPTY);
    }
  }
}
