package com.sagag.services.hazelcast.api;

import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Latest customer info caching expired in 1 hour and after user session ends.
 */
public interface CustomerCacheService {

  /**
   * Gets cached customer addresses.
   *
   * @param customerNr the customer
   * @param companyName
   * @return the cached customer list of {@link Address}
   */
  List<Address> getCachedCustomerAddresses(String customerNr, String companyName);

  /**
   * Gets cached customer.
   *
   * @param customerNr  the customer nr
   * @param companyName the company name
   * @return customer { @link Customer }
   */
  Optional<Customer> getCachedCustomer(String customerNr, String companyName);

  /**
   * Clears cache for specific customer number.
   *
   * @param customerNr the customer number
   */
  void clearCache(String customerNr);

}
