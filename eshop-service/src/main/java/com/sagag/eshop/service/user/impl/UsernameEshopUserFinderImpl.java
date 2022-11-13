package com.sagag.eshop.service.user.impl;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.collection.OrganisationCollection;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.user.LoginInputType;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.WholesalerUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class UsernameEshopUserFinderImpl extends AbstractEshopUserFinder {

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  protected OrganisationService orgService;

  /**
   * The functional implementation for finding user by username case sensitive.
   *
   */
  @Override
  public Optional<EshopUser> findBy(String username, String affiliate) throws UserValidationException {
    final List<EshopUser> users = searchUsers(username);
    return findValidUser(users, affiliate, username, affiliateFinderForCustomerUser());
  }

  protected List<EshopUser> searchUsers(String username) throws UserValidationException {
    if (StringUtils.isBlank(username)) {
      final String errorMsg = "The given username must not be empty";
      throw new UserValidationException(UserErrorCase.UE_NFU_001, errorMsg);
    }
    final List<EshopUser> users = userService.getUsersByUsername(username);
    if (CollectionUtils.isEmpty(users)) {
      final String errorMsg = String.format("Not found user by username = %s", username);
      throw new UserValidationException(UserErrorCase.UE_NFU_001, errorMsg);
    }
    return users;
  }

  protected Optional<EshopUser> findValidUser(final List<EshopUser> users, final String affiliate,
      final String originalUsernameInput,
      final Function<Organisation, Optional<Organisation>> affiliateFinder) {
    if (isBelongToSagGroup(affiliate)) {
      // if user does not belong to any affiliates or belong to SAG admin
      // then, the username should be unique
      return users.stream()
          .filter(filterByUsernameCaseSensitive(originalUsernameInput))
          .findFirst();
    }
    return users.stream()
        .filter(user -> isAffiliateMatched(user, affiliate, originalUsernameInput, affiliateFinder))
        .findFirst();
  }

  protected static boolean isBelongToSagGroup(final String affiliate) {
    return StringUtils.isBlank(affiliate)
        || StringUtils.equalsIgnoreCase(SagConstants.SAG, affiliate);
  }

  protected boolean isAffiliateMatched(final EshopUser user, final String shortAffName,
      final String originalUsernameInput,
      final Function<Organisation, Optional<Organisation>> affiliateFinder) {
    if (!filterByUsernameCaseSensitive(originalUsernameInput).test(user)) {
      return false;
    }
    if (isSalesSupported(user)) {
      return true; // allow Sales login with multiple affiliate urls.
    }

    final Optional<Organisation> userOrg = findParentOrg(user, affiliateFinder);
    userOrg.ifPresent(user::setAffiliate); // should rename the affiliate to parentOrg
    if (!userOrg.isPresent()) {
      return false;
    }
    return isAffiliateMatched(shortAffName, user.affiliateInUrl(userOrg.get()));
  }

  protected boolean isSalesSupported(final EshopUser user) {
    return user.isSalesAssistantRole();
  }

  protected boolean isAffiliateMatched(String affiliate, String affiliateOfEshopUser) {
    return StringUtils.equalsIgnoreCase(affiliate, affiliateOfEshopUser);
  }

  protected static Predicate<EshopUser> filterByUsernameCaseSensitive(
      final String originalUsername) {
    return user -> StringUtils.equals(originalUsername, user.getUsername());
  }

  private Optional<Organisation> findParentOrg(final EshopUser user,
      final Function<Organisation, Optional<Organisation>> affiliateFinder) {
    Optional<Organisation> orgOpt = orgService.getFirstByUserId(user.getId());
    if (sagAdminRolePredicate().test(user)) {
      return orgOpt;
    }
    if (!orgOpt.isPresent()) {
      return Optional.empty();
    }

    Optional<OrganisationCollection> orgCollection =
        orgCollectionService.getCollectionByOrgId(orgOpt.get().getId());
    if (!orgCollection.isPresent()) {
      return Optional.empty();
    }
    return affiliateFinder.apply(orgOpt.get());
  }

  private static Predicate<EshopUser> sagAdminRolePredicate() {
    return user -> user.isAdmin() || user.isGroupAdminRole();
  }

  private Function<Organisation, Optional<Organisation>> affiliateFinderForCustomerUser() {
    return org -> orgService.getByOrgId(org.getParentId());
  }

  @Override
  public boolean isMatchedFinder(String input, String affiliate) {
    return !StringUtils.isBlank(input) && !emailValidator.isValid(input, null)
        && !WholesalerUtils.isFinalCustomerEndpoint(affiliate);
  }

  @Override
  public LoginInputType inputType() {
    return LoginInputType.USERNAME;
  }
}
