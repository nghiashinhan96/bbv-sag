package com.sagag.services.article.api.domain.customer;

import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class CustomerInfo {

  private Customer customer;

  private List<Address> addresses;

  private CreditLimitInfo creditLimitInfo;

  public boolean equalsAffiliate(String affiliate) {
    if (StringUtils.isBlank(affiliate) || customer == null) {
      return false;
    }
    return StringUtils.equals(affiliate, customer.getAffiliateShortName());
  }

  public boolean isActivedCustomer() {
    return customer != null && customer.isActive();
  }

}
