package com.sagag.services.oauth2.api.impl.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.api.PermissionService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.oauth2.exception.UnsupportedAffiliateException;
import com.sagag.services.oauth2.exception.WholesalerPermisisonAccessDeniedException;
import com.sagag.services.oauth2.model.user.EshopUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class FinalCustomerUserDetailsServiceImpl extends AbstractEshopUserDetailsService {

  private final PermissionService permissionService;

  @Autowired
  public FinalCustomerUserDetailsServiceImpl(final PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @Override
  @Transactional
  @LogExecutionTime
  public UserDetails loadUserByUsername(String username) {
    log.debug("Authenticating wholesaler user with username = {}", username);
    final String affiliate = eshopAuthHelper.getLoginAffiliate();
    final EshopUser user = search(username, affiliate)
        .orElseThrow(() -> new UnsupportedAffiliateException(affiliate));

    // Check customer has wholesaler permission to refuse login
    final Organisation org = user.firstOrganisation().orElseThrow(
        () -> new IllegalArgumentException("Not found wholesaler customer of final customer user"));
    if (!permissionService.hasCollectionPermissionByOrgId(org.getParentId(),
        PermissionEnum.WHOLESALER)) {
      throw new WholesalerPermisisonAccessDeniedException("Disabled wholesaler permission");
    }

    doUpdateDateTimeOnLogin(user, NO_SUPPORTED_SALES_LOGIN);
    return EshopUserDetails.buildFinalCustomerUserDetails(user,
        eshopAuthHelper.getLocatedAffiliate(), eshopAuthHelper.isSsoLogin());
  }

  @Override
  protected Optional<EshopUser> search(String username, String affiliate) {
    log.debug("Returning wholesaler user info by username = {} - affiliate = {}",
        username, affiliate);
    try {
      return userSearchFactory.searchFinalCustomerUsername(username, affiliate);
    } catch (UserValidationException e) {
      throw usernameNotFoundExceptionSupplier(username).get();
    }
  }

  @Override
  public boolean support(String affiliate) {
    log.debug("Checking the affiliate = {} is match wholesaler mode", affiliate);
    return WholesalerUtils.isFinalCustomerEndpoint(affiliate);
  }

}
