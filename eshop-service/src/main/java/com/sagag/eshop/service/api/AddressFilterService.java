package com.sagag.eshop.service.api;

import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public interface AddressFilterService {

  List<Address> getBillingAddresses(List<Address> addresses);

  List<Address> getDeliveryAddresses(List<Address> addresses);

  default Supplier<Address> getDefaultAddress(List<Address> addresses) {
    return () -> CollectionUtils.emptyIfNull(addresses).stream()
        .filter(address -> ErpAddressType.DEFAULT.name().equalsIgnoreCase(address.getAddressTypeCode()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Default address not found"));
  }

  default Optional<Address> filterAddress(List<Address> addresses, String currentAddressId,
      ErpAddressType type) {
    if (CollectionUtils.isEmpty(addresses) && type == null) {
      throw new IllegalStateException();
    }
    if (addresses.size() == 1) {
      return Optional.of(addresses.get(0));
    }
    return Optional.of(addresses.stream()
        .filter(e -> type.name().equalsIgnoreCase(e.getAddressTypeCode()))
        .filter(address -> Objects.nonNull(address.getId()))
        .filter(address -> StringUtils.isEmpty(currentAddressId)
            || address.getId().equalsIgnoreCase(currentAddressId))
        .findFirst()
        .orElseGet(getDefaultAddress(addresses)));
  }

  default boolean updateAddressSettings() {
    return false;
  }
}
