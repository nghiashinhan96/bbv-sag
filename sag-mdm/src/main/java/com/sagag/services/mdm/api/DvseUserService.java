package com.sagag.services.mdm.api;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.mdm.dto.DvseCustomerUserInfo;

/**
 * <p>
 * The service to create customer and user for DVSE.
 * </p>
 *
 * @author thi.nguyen
 * @since 05.2017
 *
 */
public interface DvseUserService {

  /**
   * <p>
   * Creates new customer by customer name.
   * </p>
   * <pre></pre>
   *
   * @param customerName
   * @return the new customer id
   *
   */
  String createCustomer(String customerName, SupportedAffiliate affiliate);

  /**
   * <p>
   * Removes customer by customer id.
   * </p>
   * <pre></pre>
   *
   * @param customerId
   *
   */
  void removeCustomer(String customerId);

  /**
   * <p>
   * Creates new user with customer id.
   * </p>
   * <pre></pre>
   *
   * @param customerId
   * @param username
   * @param password
   * @return the new customer user info of {@link DvseCustomerUserInfo}
   *
   */
  DvseCustomerUserInfo createUser(String customerId, String username, String password, SupportedAffiliate affiliate);

  /**
   * <p>
   * Removes customer by username and password.
   * </p>
   * <pre></pre>
   *
   * @param customerId
   * @param username
   * @param password
   *
   */
  void removeUser(String customerId, String username, String password);

  /**
   * <p>
   * Gets calatog uri with username and password.
   * </p>
   * <pre></pre>
   * @param affiliate
   * @param username
   * @param password
   * @param externalSessionId tele saleId or normal userId
   * @return The uri of {@link String}
   *
   */
  String getDvseCatalogUri(SupportedAffiliate affiliate, String username, String password, String externalSessionId);

  /**
   * Checks whether dvse username existed or not.
   * @param dvseCusomerId
   * @param dvseUsername
   * @return true if username existed , otherwise return false
   */
  boolean existDvseUsername(String dvseCusomerId, String dvseUsername);

  /**
   * Checks whether dvse customer id existed or not.
   * @param dvseCusomerId
   * @return true if dvse customer id existed , otherwise return false
   */
  boolean existDvseCustomerId(String dvseCusomerId);
}
