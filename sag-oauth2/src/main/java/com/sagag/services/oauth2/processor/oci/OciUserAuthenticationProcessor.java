package com.sagag.services.oauth2.processor.oci;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.api.inner.ExternalUserPoolHelper;
import com.sagag.services.common.enums.LoginMode;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.processor.NonSaleAuthenticationProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OciUserAuthenticationProcessor implements NonSaleAuthenticationProcessor {

  private static final boolean NO_SUPPORTED_DMS = false;

  @Autowired
  private UserService userService;

  @Autowired
  private ExternalUserPoolHelper externalUserPoolHelper;

  @Autowired
  private EshopAuthHelper eshopAuthenticationHelper;

  @Override
  @Transactional
  public EshopUserDetails process(EshopUserDetails details, Object... args) {

    final String loginAffiliate = eshopAuthenticationHelper.getLoginAffiliate();
    if (WholesalerUtils.isFinalCustomerEndpoint(loginAffiliate)) {
      throw new IllegalArgumentException(
          String.format("OCI function is not supported for this affiliate %s", loginAffiliate));
    }

    long userId = details.getId();
    if (!userService.hasPermission(userId, PermissionEnum.OCI)) {
      throw new UserDeniedAuthorizationException("User does not support oci function.");
    }

    final String loginLanguage = eshopAuthenticationHelper.getLoginLanguage();
    EshopUser newEshopUser =
        userService.createVirtualUser(userId, UserType.VIRTUAL_OCI, loginLanguage);
    newEshopUser.getFirstRole();
    details = EshopUserDetails.buildVirtualUserDetails(newEshopUser,
        eshopAuthenticationHelper.getLocatedAffiliate(), NO_SUPPORTED_DMS);

    if (userService.hasPermission(userId, PermissionEnum.DVSE)) {
      externalUserPoolHelper.assignDvseUserForVirtualUser(newEshopUser);
    }
    return details;
  }

  @Override
  public LoginMode loginMode() {
    return LoginMode.OCI;
  }

}
