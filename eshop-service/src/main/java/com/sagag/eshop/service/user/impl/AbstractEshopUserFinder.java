package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.user.IEshopUserFinder;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEshopUserFinder implements IEshopUserFinder {

  @Autowired
  protected UserService userService;

  @Autowired
  protected EmailValidator emailValidator;

}
