package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.ax.AxCustomerInfo;

import java.util.List;
import java.util.Optional;

/**
 * Interface to build some specific for AX services.
 *
 */
public interface AxService {

  /**
   * Returns the active customer info include the list of addresses.
   *
   * @param compName
   * @param custNr
   * @return the optional of <code>{@link AxCustomerInfo}</code>
   */
  Optional<AxCustomerInfo> getActiveCustomerInfo(String compName, String custNr);

  /**
   * Returns the customer addresses by customer number.
   *
   * @param companyName the company name to search
   * @param customerNr the customer number
   * @return the list of <code>Address</code> instance
   */
  List<Address> searchCustomerAddresses(String companyName, String customerNr);

}
