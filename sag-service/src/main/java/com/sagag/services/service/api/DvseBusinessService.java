package com.sagag.services.service.api;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;

public interface DvseBusinessService {

  /**
   * Creates DVSE user.
   *
   * @param userId the referring userId
   * @param affiliate the affiliate
   * @param externalCustomerId
   */
  void createDvseUserInfo(long userId, SupportedAffiliate affiliate, int orgId)
      throws UserValidationException, MdmCustomerNotFoundException;

  /**
   * Creates dvse customer in eshop.
   *
   * @param organisation the organisation to refer
   * @param affiliate the affiliate of customer
   * @return external customer Id
   */
  String createDvseCustomerInfo(int orgId, SupportedAffiliate affiliate);

  /**
   * Creates virtual dvse user pool.
   *
   * @param orgId the orgId to refer
   * @param externalCustomerId the external customer id
   * @param affiliate the affiliate of customer
   * @param virtualPoolSize the virtual pool size expected
   */
  void createVirtualDvseUserPool(int orgId, String externalCustomerId, SupportedAffiliate affiliate, int virtualPoolSize);

}
