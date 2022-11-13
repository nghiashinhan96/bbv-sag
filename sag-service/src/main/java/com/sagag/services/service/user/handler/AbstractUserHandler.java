package com.sagag.services.service.user.handler;

import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.UserSettingsService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.service.payment.UserPaymentSettingsFormBuilder;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractUserHandler {

  @Autowired
  protected UserService userService;

  @Autowired
  protected CustomerExternalService customerExtService;

  @Autowired
  protected UserPaymentSettingsFormBuilder userPaymentSettingsFormBuilder;

  @Autowired
  protected CustomerSettingsService custSettingsService;

  @Autowired
  protected UserSettingsService userSettingsService;

  @Autowired
  protected EshopRoleRepository eshopRoleRepository;

}
