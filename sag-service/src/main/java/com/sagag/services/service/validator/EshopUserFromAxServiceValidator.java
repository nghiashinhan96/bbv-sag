package com.sagag.services.service.validator;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.service.validator.criteria.AxUserValidateCriteria;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Validation implementation for checking E-shop user is belong to the existing customer.
 */
@Component
@Slf4j
public class EshopUserFromAxServiceValidator implements IDataValidator<AxUserValidateCriteria> {

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private CustomerExternalService customerExtService;

  /**
   * Checks valid EShop user with ERP system.
   *
   * @param criteria the validation criteria
   */
  @Override
  public boolean validate(final AxUserValidateCriteria criteria) {
    log.debug("Validate eshop user is belong to the existing customer from AX services.");
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notNull(criteria.getEshopUser(), "The given eshop user must not be null");
    Assert.hasText(criteria.getAffiliate(), "The given affiliate must not be empty");

    final EshopUser user = criteria.getEshopUser();
    final String affiliateId = criteria.getAffiliate();

    final Organisation organisation =
        orgService.getFirstByUserId(user.getId()).orElseThrow(IllegalArgumentException::new);

    final Organisation parentOrg = orgService.getByOrgId(organisation.getParentId())
        .orElseThrow(IllegalArgumentException::new);

    if (!StringUtils.isBlank(affiliateId)
        && isAccessDenied(affiliateId, user, organisation, parentOrg)) {
      final String msg = String.format("the affiliate %s does not match", affiliateId);
      log.error("login denied. wrong affiliate with msg = {}", msg);
      throw new AccessDeniedException(msg);
    }
    // Check to ERP system
    if (!user.isUserAdminRole() && !user.isNormalUserRole()) {
      return true;
    }
    final String companyName = user.companyName(parentOrg);
    final String customerNr = organisation.getCustomerNrByOrg();
    return customerExtService.findCustomerByNumber(companyName, customerNr).isPresent();
  }

  public boolean validate(final EshopUser eshopUser, final String affiliateId) {
    return validate(new AxUserValidateCriteria(eshopUser, affiliateId));
  }

  private static boolean isAccessDenied(final String affiliateId, final EshopUser user,
      final Organisation organisation, final Organisation parentOrg) {
    if (user.isGroupAdminRole()) {
      return !StringUtils.equals(organisation.getShortname(), affiliateId);
    }
    return !StringUtils.equals(parentOrg.getShortname(), affiliateId);
  }
}
