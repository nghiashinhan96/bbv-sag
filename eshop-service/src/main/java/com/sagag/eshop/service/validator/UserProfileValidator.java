package com.sagag.eshop.service.validator;

import com.sagag.eshop.repo.api.VUserDetailRepository;
import com.sagag.eshop.service.exception.InvalidUserProfileException;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.eshop.service.exception.UsernameDuplicationException;
import com.sagag.eshop.service.validator.criteria.UserProfileValidateCriteria;
import com.sagag.eshop.service.validator.criteria.UsernameDuplicationCriteria;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

import javax.validation.ConstraintViolation;

/**
 * Validation implementation for checking user profile info.
 */
@Component
@Slf4j
public class UserProfileValidator implements IDataValidator<UserProfileValidateCriteria> {

  @Autowired
  private VUserDetailRepository vUserDetailRepo;

  @Autowired
  private UsernameDuplicationValidator duplidatedUserNameSameAffiliateValidator;

  @Override
  public boolean validate(UserProfileValidateCriteria criteria) throws ValidationException {
    log.debug("Validate user profile");
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notNull(criteria.getUserProfile(), "The given user profile must not be null");
    Assert.hasText(criteria.getAffiliateShortName(), "The given affiliate name must not be empty");

    final UserProfileDto userProfile = criteria.getUserProfile();
    final String affiliate = criteria.getAffiliateShortName();

    final Optional<ConstraintViolation<UserProfileDto>> violationOpt =
        SagValidationUtils.validateObjectAndReturnFirstError(userProfile);
    if (violationOpt.isPresent()) {
      throw new InvalidUserProfileException(userProfile);
    }

    // start validate user name. Id should always exist or 0. Not update the same time issue.
    if (userProfile.getId() != 0) {
      final String existingUserName = findUsernameByUserId(userProfile.getId());
      if (StringUtils.equalsIgnoreCase(existingUserName, userProfile.getUserName())) {
        return true;
      }
    }
    return duplidatedUserNameSameAffiliateValidator
        .validate(new UsernameDuplicationCriteria(userProfile.getUserName(), affiliate));
  }

  private String findUsernameByUserId(final long userId) throws UserValidationException {
    final Optional<String> userNameOpt = vUserDetailRepo.findUsernameByUserId(userId);
    if (!userNameOpt.isPresent()) {
      final String msg = String.format("Not found any userName with id = %d", userId);
      throw new UserValidationException(UserErrorCase.UE_NFU_001, msg);
    }
    return userNameOpt.get();
  }

  public boolean validateWithUserValidationException(UserProfileValidateCriteria criteria)
    throws UserValidationException {
    try {
      return validate(criteria);
    } catch (ValidationException ex) {
      throw handleUserValidationException(ex);
    }
  }

  private UserValidationException handleUserValidationException(ValidationException ex) {
    if (ex instanceof InvalidUserProfileException) {
      return new UserValidationException(UserErrorCase.UE_IUN_001, ex.getMessage());
    } else if (ex instanceof UsernameDuplicationException) {
      return new UserValidationException(UserErrorCase.UE_DUA_001, ex.getMessage());
    } else if (ex instanceof UserValidationException) {
      return (UserValidationException) ex;
    }
    return new UserValidationException(UserErrorCase.UE_NFU_001, ex.getMessage());
  }

}
