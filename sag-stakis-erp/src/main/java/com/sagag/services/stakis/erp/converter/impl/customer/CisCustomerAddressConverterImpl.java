package com.sagag.services.stakis.erp.converter.impl.customer;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.stakis.erp.converter.CisCustomerDataConverter;
import com.sagag.services.stakis.erp.enums.StakisCustomerDataType;
import com.sagag.services.stakis.erp.wsdl.cis.CustomerDetails;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@Component
@Order(2)
@CzProfile
public class CisCustomerAddressConverterImpl implements CisCustomerDataConverter<List<Address>> {

  private static final String DEFAULT_OR_INVOICE_ADDRESS_TYPE = "0";

  @Override
  public List<Address> apply(OutCustomer outCustomer) {
    final CustomerDetails custDetails = outCustomer.getCustomer().getValue();
    final String customerId = getValueOpt(custDetails.getId()).orElse(StringUtils.EMPTY);
    return custDetails.getAddresses().getValue().getAddress().stream()
        .map(cisAddr -> {
          final Address address = new Address();
          getValueOpt(cisAddr.getId()).map(primaryAddressParser())
          .ifPresent(address::setPrimary);
          final ErpAddressType erpAddrType = erpAddressTypeParser().apply(address.isPrimary());
          address.setAddressType(erpAddrType.name());
          address.setAddressTypeCode(erpAddrType.name());
          getValueOpt(cisAddr.getDescription()).ifPresent(address::setAddressTypeDesc);

          getValueOpt(cisAddr.getStreet()).ifPresent(address::setStreet);
          getValueOpt(cisAddr.getCity()).ifPresent(address::setCity);
          getValueOpt(cisAddr.getCountry()).ifPresent(address::setCountry);
          getValueOpt(cisAddr.getPostOfficeBox()).ifPresent(address::setPostOfficeBox);

          getValueOpt(cisAddr.getZIP()).ifPresent(zipCode -> {
            // #1574: [ERP Module Integration] Get Customer: customer info, addresses, etc.
            // Combine customerId + ZIP as address id
            address.setId(customerId + zipCode);
            address.setPostCode(zipCode);
          });
          return address;
        }).collect(Collectors.toList());
  }

  private static Function<String, Boolean> primaryAddressParser() {
    return addId -> StringUtils.equals(DEFAULT_OR_INVOICE_ADDRESS_TYPE, addId);
  }

  private static Function<Boolean, ErpAddressType> erpAddressTypeParser() {
    return isPrimary -> isPrimary ? ErpAddressType.INVOICE : ErpAddressType.DELIVERY;
  }

  @Override
  public void accept(CustomerInfo customerInfo, OutCustomer response) {
    customerInfo.setAddresses(apply(response));
  }

  @Override
  public StakisCustomerDataType type() {
    return StakisCustomerDataType.CUSTOMER_ADDRESSES;
  }

}
