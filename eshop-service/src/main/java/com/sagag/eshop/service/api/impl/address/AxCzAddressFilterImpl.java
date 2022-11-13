package com.sagag.eshop.service.api.impl.address;

import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.profiles.AxCzProfile;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AxCzProfile
public class AxCzAddressFilterImpl implements AddressFilterService {

  @Override
  public List<Address> getBillingAddresses(List<Address> addresses) {
    return ListUtils.emptyIfNull(addresses);
  }

  @Override
  public List<Address> getDeliveryAddresses(List<Address> addresses) {
    if (CollectionUtils.isEmpty(addresses)) {
      return Lists.emptyList();
    }
    if (addresses.stream().anyMatch(erpAddressTypePredicate(ErpAddressType.DELIVERY))) {
      return addresses.stream().filter(erpAddressTypePredicate(ErpAddressType.DELIVERY))
          .collect(Collectors.toList());
    }
    return addresses.stream().filter(erpAddressTypePredicate(ErpAddressType.DEFAULT))
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Address> filterAddress(List<Address> addresses, String currentAddressId,
                                         ErpAddressType type) {
    if (CollectionUtils.isEmpty(addresses) && Objects.isNull(type)) {
      throw new IllegalStateException();
    }
    if (addresses.size() == 1) {
      return addresses.stream().findFirst();
    }
    return Optional.of(addresses.stream()
        .filter(e -> type.name().equalsIgnoreCase(e.getAddressTypeCode()))
        .filter(address -> Objects.nonNull(address.getId()))
        .findFirst()
        .orElseGet(getDefaultAddress(addresses)));
  }

  @Override
  public boolean updateAddressSettings() {
    return true;
  }

  private Predicate<? super Address> erpAddressTypePredicate(ErpAddressType addressType) {
    return address -> addressType.name().equalsIgnoreCase(address.getAddressType());
  }

}
