package com.sagag.services.oauth2.processor.dms;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.inner.ExternalUserPoolHelper;
import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.hazelcast.api.ActiveDmsUserCacheService;
import com.sagag.services.hazelcast.model.ActiveUser;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.processor.NonSaleAuthenticationProcessor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractDmsUserAuthenticationProcessor
    implements NonSaleAuthenticationProcessor {

  @Autowired
  private UserService userService;

  @Autowired
  private ActiveDmsUserCacheService activeDmsUserCacheService;

  @Autowired
  private EshopAuthHelper eshopAuthenticationHelper;

  @Autowired
  private ExternalUserPoolHelper externalUserPoolHelper;

  @Override
  @Transactional
  public EshopUserDetails process(EshopUserDetails details, Object... args) {
    return doProcess(details);
  }

  private EshopUserDetails doProcess(EshopUserDetails details) {
    long userId = details.getId();
    if (!userService.hasPermission(userId, PermissionEnum.DMS)) {
      throw new UserDeniedAuthorizationException("User does not support dms function.");
    }
    if (activeDmsUserCacheService.contains(userId)) {
      EshopUser newEshopUser =
          userService.createVirtualUser(userId, UserType.VIRTUAL_DMS, StringUtils.EMPTY);

      details = EshopUserDetails.buildVirtualUserDetails(newEshopUser,
          eshopAuthenticationHelper.getLocatedAffiliate(), supportCloud());
      if (WholesalerUtils.isFinalCustomerEndpoint(eshopAuthenticationHelper.getLoginAffiliate())) {
        return details;
      }

      if (userService.hasPermission(userId, PermissionEnum.DVSE)) {
        externalUserPoolHelper.assignDvseUserForVirtualUser(newEshopUser);
      }
      return details;
    }

    ActiveUser activeUser = ActiveUser.builder().id(userId).loginMode(loginMode())
        .loginTime(System.currentTimeMillis()).build();
    activeDmsUserCacheService.put(activeUser);
    return details;
  }

  private boolean supportCloud() {
    return LoginMode.CLOUD_DMS == this.loginMode();
  }

}
