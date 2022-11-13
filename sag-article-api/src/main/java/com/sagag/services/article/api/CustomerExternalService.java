package com.sagag.services.article.api;

import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;

import java.util.List;
import java.util.Optional;

public interface CustomerExternalService {

  /**
   * Returns the customer from the customer number and its company.
   *
   * @param companyName the company name.
   * @param customerNr the customer number.
   * @return the {@link Customer} with the input number.
   */
  Optional<Customer> findCustomerByNumber(String companyName, String customerNr);

  /**
   * Returns the customer addresses by customer number.
   *
   * @param companyName the company name to search
   * @param customerNr the customer number
   * @return the list of <code>Address</code> instance
   */
  List<Address> searchCustomerAddresses(String companyName, String customerNr);

  /**
   * Returns the customer address detail by addressId.
   *
   * @param company the company name
   * @param customerNr the customer number
   * @param addressId the address id
   * @return the optional of {@link Address}
   *
   */
  Optional<Address> findCustomerAddressById(String company, String customerNr, String addressId);

  /**
   * Retrieves representation of a warehouse which is given by its id.
   *
   * @param branchId the branch id of affiliate
   * @return the branch info of {@link CustomerBranch}
   */
  Optional<CustomerBranch> searchBranchById(String companyName, String branchId);

  /**
   * <p>
   * Retrieves warehouse information.
   * </p>
   *
   * @param companyName the company name
   * @param defaultBranchId the default branch id
   * @param isSaleOnBehalf
   * @return the list of <code>CustomerBranch</code> instance.
   */
  List<CustomerBranch> getCustomerBranches(String companyName, String defaultBranchId, boolean isSaleOnBehalf);

  /**
   * Returns the credit limit info by customer number.
   *
   * @param compName the company name
   * @param custNr the customer number
   * @return the optional of {@link AxCreditLimitInfo}
   */
  Optional<CreditLimitInfo> getCreditLimitInfoByCustomer(String compName, String custNr);

  /**
   * Returns the active customer info include the list of addresses.
   *
   * @param compName
   * @param custNr
   * @return the optional of <code>{@link AxCustomerInfo}</code>
   */
  Optional<CustomerInfo> getActiveCustomerInfo(String compName, String custNr);
}
