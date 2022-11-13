package com.sagag.services.service.utils;

import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.sag.external.Customer;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserDataProvider {

  public static UserInfo buildOfferUserInfo() {
    UserInfo user = new UserInfo();
    user.setId(27L);
    user.setUsername("tuan1.ax");
    user.setOrganisationId(41);
    Customer customer = new Customer();
    customer.setNr(1130438);
    customer.setName("Heinisch & Partner");
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    return user;
  }

  public static UserInfo buildOrderUserInfo() {
    UserInfo user = new UserInfo();
    user.setId(69L);
    user.setUsername("duongdang.at.ax");
    user.setOrganisationId(42);
    Customer customer = new Customer();
    customer.setNr(1130438);
    customer.setName("Heinisch & Partner");
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer);
    user.setSettings(ownSettings);
    return user;
  }
}
