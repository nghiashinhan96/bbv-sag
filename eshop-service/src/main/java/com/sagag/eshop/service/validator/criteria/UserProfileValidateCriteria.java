package com.sagag.eshop.service.validator.criteria;

import com.sagag.services.domain.eshop.dto.UserProfileDto;

import lombok.Data;

/**
 * Criteria for validate user profile.
 */
@Data
public class UserProfileValidateCriteria {

  private final UserProfileDto userProfile;

  private final String affiliateShortName;

}
