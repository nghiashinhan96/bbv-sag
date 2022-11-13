package com.sagag.services.service.customer.registration;

import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.validator.criteria.UserProfileValidateCriteria;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.UserProfileDto;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.validator.ApmUserAdminValidator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class APMCustomerRegistrationHandler extends AbstractCustomerRegistrationHandler {

  @Autowired
  private ExternalOrganisationRepository extOrgRepo;

  @Autowired
  private ApmUserAdminValidator apmUserAdminValidator;

  @Override
  protected void createCustomerAdminUser(UserRegistrationDto userRegistration,
      String defaultPassword, SupportedAffiliate affiliate, Organisation customerOrg,
      boolean isDvseCustomer, String language)
      throws UserValidationException, MdmCustomerNotFoundException {
    // Create user admin user of customer
    createUserAdminUserAndDvseUser(userRegistration, defaultPassword, affiliate, customerOrg,
        language);
  }

  @Override
  protected EshopUser createUserAdminUserAndDvseUser(UserRegistrationDto userRegistrationDto,
      String defaultPassword, SupportedAffiliate affiliate, Organisation customer, String langISO)
      throws UserValidationException, MdmCustomerNotFoundException {
    final UserProfileDto userProfileDto =
        SagBeanUtils.map(userRegistrationDto, UserProfileDto.class);
    final UserProfileValidateCriteria validateCriteria =
        new UserProfileValidateCriteria(userProfileDto, affiliate.getAffiliate());
    userProfileValidator.validateWithUserValidationException(validateCriteria);

    return createEshopUserByProfileForAPMCustomer(userProfileDto, userRegistrationDto,
        Optional.empty(), customer, affiliate, langISO);
  }

  private EshopUser createEshopUserByProfileForAPMCustomer(UserProfileDto userProfile,
      UserRegistrationDto userRegistrationDto, Optional<UserType> userType, Organisation customer,
      SupportedAffiliate affiliate, String langISO)
      throws UserValidationException, MdmCustomerNotFoundException {
    final String passwordHash = userRegistrationDto.getPasswordHash();
    final String passwordSalt = userRegistrationDto.getPasswordSalt();
    final EshopUser eshopUser = userService.createEshopUserAdminForAPMCustomer(userProfile,
        passwordHash, passwordSalt, userType, customer, affiliate, langISO);
    final int orgId = customer.getId();

    final String extOrgId =
        extOrgRepo.findExternalCustomerIdByOrgIdAndExternalApp(orgId, ExternalApp.DVSE);
    if (StringUtils.isBlank(extOrgId)) {
      dvseBusinessService.createDvseCustomerInfo(orgId, affiliate);
    }
    dvseBusinessService.createDvseUserInfo(eshopUser.getId(), affiliate, orgId);
    return eshopUser;
  }

  @Override
  protected void validateUserRegistration(UserRegistrationDto userRegistration)
      throws UserValidationException {
    apmUserAdminValidator.validate(userRegistration);
  }
}
