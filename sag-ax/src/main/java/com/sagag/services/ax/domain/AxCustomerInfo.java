package com.sagag.services.ax.domain;

import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.ax.utils.AxAddressUtils;
import com.sagag.services.domain.sag.erp.Address;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AxCustomerInfo extends CustomerInfo {

  public boolean equalsDefaultPostCode(String postCode) {
    if (StringUtils.isBlank(postCode) || CollectionUtils.isEmpty(getAddresses())) {
      return false;
    }
    final Optional<Address> defaultAddrOpt = getDefaultAddress();
    return defaultAddrOpt.isPresent()
        && StringUtils.equalsIgnoreCase(defaultAddrOpt.get().getPostCode(), postCode);
  }

  public boolean equalsInvoicePostCode(String postCode) {
    if (StringUtils.isBlank(postCode) || CollectionUtils.isEmpty(getAddresses())) {
      return false;
    }
    final Optional<Address> defaultAddrOpt = getInvoiceAddress();
    return defaultAddrOpt.isPresent()
        && StringUtils.equalsIgnoreCase(defaultAddrOpt.get().getPostCode(), postCode);
  }

  public Optional<Address> getDefaultAddress() {
    return AxAddressUtils.findDefaultAddress(getAddresses());
  }

  public Optional<Address> getInvoiceAddress() {
    return AxAddressUtils.findInvoiceAddress(getAddresses());
  }

  public static AxCustomerInfo of(CustomerInfo customerInfo) {
    AxCustomerInfo axCustomerInfo = new AxCustomerInfo();
    axCustomerInfo.setCustomer(customerInfo.getCustomer());
    axCustomerInfo.setAddresses(customerInfo.getAddresses());
    axCustomerInfo.setCreditLimitInfo(customerInfo.getCreditLimitInfo());
    return axCustomerInfo;
  }


}
