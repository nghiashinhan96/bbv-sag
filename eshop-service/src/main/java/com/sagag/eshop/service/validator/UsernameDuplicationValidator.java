package com.sagag.eshop.service.validator;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UsernameDuplicationException;
import com.sagag.eshop.service.validator.criteria.UsernameDuplicationCriteria;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.common.validator.IDataValidator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Validation implementation for checking duplicated user name in same affiliate.
 */
@Component
@Slf4j
public class UsernameDuplicationValidator implements IDataValidator<UsernameDuplicationCriteria> {

  @Autowired
  private UserService userService;

  @Autowired
  private OrganisationService orgService;

  @Override
  public boolean validate(UsernameDuplicationCriteria criteria)
      throws UsernameDuplicationException {
    log.debug("Validating duplicated userName in same affiliate = {}", criteria);
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.hasText(criteria.getUserName(), "The given user name must not be empty");
    Assert.hasText(criteria.getAffiliate(), "The given affiliate must not be empty");

    final List<EshopUser> existingUsers = userService.getUsersByUsername(criteria.getUserName());
    return validateExistingUsersWithAffiliate(existingUsers, criteria.getAffiliate());
  }

  private boolean validateExistingUsersWithAffiliate(final List<EshopUser> existingUsers,
      final String affiliate) throws UsernameDuplicationException {
    if (CollectionUtils.isEmpty(existingUsers)) {
      return false;
    }
    Organisation organisation;
    Organisation parentOrganisation;
    for (EshopUser user : existingUsers) {
      organisation = orgService.getFirstByUserId(user.getId()).orElse(null);
      if (organisation == null) {
        continue;
      }

      // Because with each user in loop will get parent org again, if the list of user is greater
      // than 10...
      if (WholesalerUtils.isFinalCustomer(organisation.getOrgCode())) {
        organisation = orgService.getByOrgId(organisation.getParentId())
            .orElseThrow(() -> new IllegalArgumentException("Wholesaler not found"));
      }
      parentOrganisation = orgService.getByOrgId(organisation.getParentId()).orElse(null);

      String affiliateName = null;
      if (user.hasToCheckTheAffiliate()) {
        if (user.isGroupAdminRole()) {
          affiliateName = organisation.getShortname();
        } else {
          affiliateName = Optional.ofNullable(parentOrganisation)
              .map(Organisation::getShortname)
              .orElse(StringUtils.EMPTY);
        }
      }

      // Check user name duplication
      if (StringUtils.equals(affiliate, affiliateName)) {
        throw new UsernameDuplicationException(user.getUsername(), affiliateName);
      }
    }
    return true;
  }

}
