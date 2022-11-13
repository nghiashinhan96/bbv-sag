package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.external.Customer;
import com.sagag.services.tools.domain.target.CustomerSettings;

import java.util.List;

public interface CustomerSettingsService {

  /**
   * Creates customer settings.
   *
   * @param customer
   * @param addresses
   * @return the object of {@link CustomerSettings}
   */
  CustomerSettings createCustomerSettings(Customer customer, List<Address> addresses);
}
