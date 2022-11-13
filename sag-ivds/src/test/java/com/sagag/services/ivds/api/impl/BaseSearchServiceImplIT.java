package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.sag.external.Customer;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/**
 * Base Search service implementation IT.
 */
public class BaseSearchServiceImplIT {

  protected static final SupportedAffiliate DERENDINGER_AT = SupportedAffiliate.DERENDINGER_AT;

  @Autowired
  private CustomerExternalService custExternalService;

  protected UserInfo user;

  @Before
  public void init() {
    if (user != null) {
      return;
    }
    final String customerNr = "1127186";
    user = loadUserInfo(DERENDINGER_AT, customerNr);
  }

  protected UserInfo loadUserInfo(SupportedAffiliate affliate, String custNr) {
    this.user = new UserInfo();
    user.setId(26l);
    String customerNr = custNr;
    user.setCompanyName(affliate.getCompanyName());
    user.setAffiliateShortName(affliate.getAffiliate());
    final Optional<Customer> customer =
        custExternalService.findCustomerByNumber(user.getCompanyName(), customerNr);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(customer.orElse(null));
    ownSettings.setPriceTypeDisplayEnum(PriceDisplayTypeEnum.UVPE_OEP_GROSS);
    user.setSettings(ownSettings);
    user.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    LocaleContextHolder.setLocale(Locale.GERMAN);
    return user;
  }

}
