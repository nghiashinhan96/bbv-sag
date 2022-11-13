package com.sagag.eshop.service.api.impl.address;

import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.services.common.profiles.AtSbProfile;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AtSbProfile
public class AtAddressFilterImpl implements AddressFilterService {

  @Override
  public List<Address> getBillingAddresses(List<Address> addresses) {
    return ListUtils.emptyIfNull(addresses);
  }

  @Override
  public List<Address> getDeliveryAddresses(List<Address> addresses) {
    return ListUtils.emptyIfNull(addresses);
  }
}
