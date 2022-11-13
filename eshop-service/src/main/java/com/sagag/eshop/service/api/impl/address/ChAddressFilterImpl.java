package com.sagag.eshop.service.api.impl.address;

import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.profiles.ChProfile;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@ChProfile
public class ChAddressFilterImpl implements AddressFilterService {

  @Override
  public Optional<Address> filterAddress(List<Address> addresses, String currentAddressId,
      ErpAddressType type) {
    if (CollectionUtils.isEmpty(addresses) && type == null) {
      throw new IllegalStateException();
    }
    if (ErpAddressType.INVOICE == type) {
      return Optional.of(CollectionUtils.emptyIfNull(addresses).stream()
          .filter(address -> type.name().equalsIgnoreCase(address.getAddressTypeCode()))
          .findFirst()
          .orElseGet(getDefaultAddress(addresses)));
    }
    return AddressFilterService.super.filterAddress(addresses, currentAddressId, type);
  }

  @Override
  public List<Address> getBillingAddresses(List<Address> addresses) {
    return Arrays.asList(filterAddress(addresses, StringUtils.EMPTY, ErpAddressType.INVOICE)
        .orElseGet(Address::new));
  }

  @Override
  public List<Address> getDeliveryAddresses(List<Address> addresses) {
    return ListUtils.emptyIfNull(addresses);
  }
}
