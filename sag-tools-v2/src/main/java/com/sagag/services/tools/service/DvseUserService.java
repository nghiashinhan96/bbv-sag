package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.mdm.DvseCustomerUserInfo;
import com.sagag.services.tools.domain.target.CustomExtOrganisation;
import com.sagag.services.tools.domain.target.CustomMdmCustomerAndUsersResponse;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.support.SupportedAffiliate;

import java.util.List;

/**
 * The service to create customer and user for DVSE.
 */
public interface DvseUserService {

  /**
   * <p>
   * Create new user with customer id.
   * </p>
   *
   * <pre></pre>
   *
   * @param customerId
   * @param username
   * @param password
   * @return the new customer user info of {@link DvseCustomerUserInfo}
   * @throws Exception
   *
   */
  DvseCustomerUserInfo createUser(String customerId, String username, String password, SupportedAffiliate affiliate) throws Exception;

  /**
   * Check whether dvse customer id existed or not.
   *
   * @param dvseCusomerId
   * @return true if dvse customer id existed , otherwise return false
   * @throws Exception
   */
  boolean existDvseCustomerId(String dvseCusomerId) throws Exception;

  /**
   * Check whether dvse username existed or not.
   *
   * @param dvseCusomerId
   * @param dvseUsername
   * @return true if username existed , otherwise return false
   */
  boolean existDvseUsername(String dvseCusomerId, String dvseUsername);

  boolean cleanCustomerAndUsers(CustomExtOrganisation extOrg, List<ExternalUser> externalUsers, SupportedAffiliate affiliate);
  
  CustomMdmCustomerAndUsersResponse createCustomerAndUsers(CustomExtOrganisation extOrg, List<ExternalUser> externalUsers, SupportedAffiliate affiliate);

  /**
   * <p>
   * Create new customer by customer name.
   * </p>
   * <pre></pre>
   *
   * @param customerName
   * @return the new customer id
   *
   */
  String createCustomer(String customerName, SupportedAffiliate affiliate);

}
