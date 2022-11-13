package com.sagag.services.incentive.api.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.api.TrainingSingleSignOnService;
import com.sagag.services.incentive.authcookie.CookieCrypt;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.domain.TrainingLoginDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TrainingSingleSignOnServiceImpl implements TrainingSingleSignOnService {

  @Autowired
  private IncentiveProperties properties;

  @Override
  public TrainingLoginDto getAuthenInfo(SupportedAffiliate affiliate, Long companyId, String userId,
      String firstName, String lastName) {
    Assert.notNull(properties.getTraining(), "Training configuration must not be null");
    Assert.notNull(companyId, "companyId must not be null");
    Assert.notNull(userId, "userId must not be null");
    Assert.hasText(firstName, "firstName must not be empty");
    Assert.hasText(lastName, "lastName must not be empty");

    final String fs = buildFs(companyId, userId, firstName, lastName);
    return TrainingLoginDto.builder().userId(userId).companyId(companyId).firstName(firstName)
        .lastName(lastName).fs(fs).build();
  }

  private String buildFs(Long companyId, String userId, String firstName, String lastName) {
    final String companyPassword = properties.getTraining().getCompanyPassword();
    Assert.hasText(companyPassword, "companyPassword must not be empty");

    final String hashEncrypt = properties.getTraining().getHashEncrypt();
    Assert.hasText(hashEncrypt, "hashEncrypt must not be empty");

    final String key = new StringBuilder().append(companyPassword).append(companyId)
        .append(companyPassword).append(userId).append(companyPassword).append(firstName)
        .append(companyPassword).append(lastName).toString();
    return CookieCrypt.createHashKey(hashEncrypt, key);
  }
}
