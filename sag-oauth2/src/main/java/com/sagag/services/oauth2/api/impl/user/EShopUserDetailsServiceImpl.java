package com.sagag.services.oauth2.api.impl.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.AutonetCompanyUtils;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.oauth2.exception.UnsupportedAffiliateException;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.settings.UserSettingsProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service handles user info, and it's setting on authorization.
 */
@Service
@Slf4j
public class EShopUserDetailsServiceImpl extends AbstractEshopUserDetailsService {

  private final UserSettingsProcessor userSettingsProcessor;

  @Autowired
  public EShopUserDetailsServiceImpl(final UserSettingsProcessor userSettingsProcessor) {
    this.userSettingsProcessor = userSettingsProcessor;
  }

  @Override
  @Transactional
  @LogExecutionTime
  public EshopUserDetails loadUserByUsername(final String username) {
    log.debug("Authenticating connect user with username = {}", username);
    final String affiliate = eshopAuthHelper.getLoginAffiliate();
    final String salesToken = eshopAuthHelper.getLoginSalesToken();
    final Long salesId = retrieveSalesIdFromToken(salesToken);

    final EshopUser user =
        search(username, affiliate).orElseThrow(() -> new UnsupportedAffiliateException(affiliate));

    doUpdateDateTimeOnLogin(user, salesId);
    if (user.hasToCheckTheCustomer()) {
      userSettingsProcessor.process(user);
    }

    return EshopUserDetails.buildEshopUserDetails(user, salesId,
        eshopAuthHelper.getLocatedAffiliate(), eshopAuthHelper.isSsoLogin());
  }

  @Override
  public boolean support(String affiliate) {
    log.debug("Checking the affiliate = {} is match connect user mode", affiliate);
    return !WholesalerUtils.isFinalCustomerEndpoint(affiliate)
        && !AutonetCompanyUtils.isAutonetEndpoint(affiliate) && !eshopAuthHelper.isCloudDmsLogin();
  }
}
