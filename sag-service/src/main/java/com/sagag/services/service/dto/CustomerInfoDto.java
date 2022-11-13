package com.sagag.services.service.dto;

import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.ax.utils.AxAddressUtils;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * POJO class for customer info.
 */
@Data
@NoArgsConstructor
public class CustomerInfoDto implements Serializable {

  private static final long serialVersionUID = 3390516510726018757L;

  private Customer customer;

  private Address defaultAddress;

  private Address deliveryAddress;

  private Address invoiceAddress;

  private CreditLimitInfo creditLimit;

  public CustomerInfoDto(Customer customer, CreditLimitInfo creditLimit,
      List<Address> addresses) {
    setCustomer(customer);
    setCreditLimit(creditLimit);
    setDefaultAddress(AxAddressUtils.findDefaultAddress(addresses).orElse(null));
    setDeliveryAddress(AxAddressUtils.findDeliveryAddress(addresses).orElse(null));
    setInvoiceAddress(AxAddressUtils.findInvoiceAddress(addresses).orElse(null));
  }

}
