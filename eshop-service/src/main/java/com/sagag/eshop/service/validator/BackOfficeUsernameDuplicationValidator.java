package com.sagag.eshop.service.validator;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.exception.UsernameDuplicationException;
import com.sagag.eshop.service.validator.criteria.BackOfficeUsernameDuplicationCriteria;
import com.sagag.eshop.service.validator.criteria.UsernameDuplicationCriteria;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Validation implementation for checking duplicated user name in same affiliate in back office.
 */
@Component
@Slf4j
public class BackOfficeUsernameDuplicationValidator
    implements IDataValidator<BackOfficeUsernameDuplicationCriteria> {

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private UsernameDuplicationValidator usernameDuplicationValidator;

  @Autowired
  private EmailValidator emailValidator;

  @Override
  public boolean validate(BackOfficeUsernameDuplicationCriteria criteria)
      throws UserValidationException {
    log.debug("Validating duplicated userName in same affiliate = {}", criteria);
    Assert.notNull(criteria, "The given criteria must not be null");
    BackOfficeUserSettingDto backOfficeUserSettingDto = criteria.getBackOfficeUserSettingDto();
    backOfficeUserSettingDto
        .setUserName(StringUtils.trimToEmpty(backOfficeUserSettingDto.getUserName()));

    if (StringUtils.isBlank(backOfficeUserSettingDto.getUserName())) {
      throw new UserValidationException(UserErrorCase.UE_IUN_001, "Username is not valid.");
    }

    if (StringUtils.isBlank(backOfficeUserSettingDto.getFirstName())) {
      throw new UserValidationException(UserErrorCase.UE_ISN_001, "Surname is not valid.");
    }
    if (!emailValidator.isValid(backOfficeUserSettingDto.getEmail(), null)) {
      throw new UserValidationException(UserErrorCase.UE_IEM_001, "Email is not valid.");
    }

    EshopUser eshopUser = criteria.getEshopUser();
    if (eshopUser.getUsername().equals(backOfficeUserSettingDto.getUserName())) {
      return true;
    }
    final String affiliateName = getUserAffiliateName(eshopUser.getId());
    UsernameDuplicationCriteria subSriteria =
        new UsernameDuplicationCriteria(backOfficeUserSettingDto.getUserName(), affiliateName);
    try {
      return usernameDuplicationValidator.validate(subSriteria);
    } catch (UsernameDuplicationException e) {
      throw new UserValidationException(UserErrorCase.UE_IAU_001,
          "User name is not allowed the duplication in the same affiliate.");
    }
  }

  private String getUserAffiliateName(final long userId) {
    final Optional<Organisation> organisation = orgService.getFirstByUserId(userId);
    if (!organisation.isPresent()) {
      return StringUtils.EMPTY;
    }
    final Optional<Organisation> parentOrganisation =
        orgService.getByOrgId(organisation.get().getParentId());
    if (!parentOrganisation.isPresent()) {
      return StringUtils.EMPTY;
    }
    return parentOrganisation.get().getShortname();
  }

}
