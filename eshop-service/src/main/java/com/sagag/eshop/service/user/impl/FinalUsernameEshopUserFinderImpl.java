package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class FinalUsernameEshopUserFinderImpl extends UsernameEshopUserFinderImpl {

  private static final String[] SUPPORTED_AUTHORITIES = {
      EshopAuthority.FINAL_USER_ADMIN.name(),
      EshopAuthority.FINAL_NORMAL_USER.name() };

  @Override
  public Optional<EshopUser> findBy(String username, String affiliate) throws UserValidationException {
    final List<EshopUser> users = searchUsers(username).stream()
        .filter(isSupportedRoles()).collect(Collectors.toList());
    return findValidUser(users, affiliate, username, afiliateFinderForWholesaler());
  }

  private Predicate<EshopUser> isSupportedRoles() {
    return user -> CollectionUtils.containsAny(user.getRoles(),
        Arrays.asList(SUPPORTED_AUTHORITIES));
  }

  private Function<Organisation, Optional<Organisation>> afiliateFinderForWholesaler() {
    return org -> {
      if (!WholesalerUtils.isFinalCustomer(org.getOrgCode())) {
        return Optional.empty();
      }

      Optional<Organisation> orgOpt = orgService.getByOrgId(org.getParentId());
      if (!orgOpt.isPresent()) {
        return Optional.empty();
      }
      return orgService.getByOrgId(orgOpt.get().getParentId());
    };
  }

  @Override
  protected boolean isSalesSupported(EshopUser user) {
    return false;
  }

  @Override
  public boolean isMatchedFinder(String input, String affiliate) {
    return !StringUtils.isBlank(input) && !emailValidator.isValid(input, null)
        && WholesalerUtils.isFinalCustomerEndpoint(affiliate);
  }

  @Override
  protected boolean isAffiliateMatched(String affiliate, String affiliateOfEshopUser) {
    return WholesalerUtils.isFinalCustomerEndpoint(affiliate);
  }

  @Override
  public LoginInputType inputType() {
    return LoginInputType.FINAL_CUSTOMER_USERNAME;
  }
}
