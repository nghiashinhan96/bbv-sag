package com.sagag.services.incentive.api;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.incentive.domain.TrainingLoginDto;

public interface TrainingSingleSignOnService {

  /**
   * Returns sso login information for training.
   *
   * @param affiliate
   * @param companyId companyId the customerNr
   * @param userId internal id of user
   * @param firstName the first name of user
   * @param lastName the last name of user
   * @return
   */
  TrainingLoginDto getAuthenInfo(SupportedAffiliate affiliate, Long companyId, String userId,
      String firstName, String lastName);
}
