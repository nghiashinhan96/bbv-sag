package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.impl.address.AtAddressFilterImpl;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(MockitoJUnitRunner.class)
public class AtAddressFilterServiceTest {

  @InjectMocks
  private AtAddressFilterImpl addressFilter;

  @Test
  public void atAddressFilter_shouldReturnTheGivenList() {
    List<Address> addressList = IntStream.range(0, 10).mapToObj(
        i -> Address.builder().addressTypeCode(i == 1 ? ErpAddressType.INVOICE.name() : ErpAddressType.DELIVERY.name())
            .build())
        .collect(Collectors.toList());
    List<Address> result = addressFilter.getBillingAddresses(addressList);
    Assert.assertThat(result, Matchers.containsInAnyOrder(addressList.toArray()));
  }

  @Test
  public void atAddressFilter_shouldReturnDefaultAddress() {
    Address invoiceAddress = Address.builder().id("invoice1").addressTypeCode(ErpAddressType.INVOICE.name()).build();
    Address defaultAddress = Address.builder().id("default1").addressTypeCode(ErpAddressType.DEFAULT.name()).build();
    Address deliveryAddress = Address.builder().id("delivery1").addressTypeCode(ErpAddressType.DELIVERY.name()).build();
    Address result =
        addressFilter.getDefaultAddress(Arrays.asList(invoiceAddress, defaultAddress, deliveryAddress)).get();
    Assert.assertThat(result, Matchers.is(defaultAddress));
  }

  @Test
  public void atAddressFilter_shouldReturnDefaultAddressIfNoDeliveryAddress() {
    Address invoiceAddress = Address.builder().id("invoice1").addressTypeCode(ErpAddressType.INVOICE.name()).build();
    Address defaultAddress = Address.builder().id("default1").addressTypeCode(ErpAddressType.DEFAULT.name()).build();
    Address result = addressFilter
        .filterAddress(Arrays.asList(invoiceAddress, defaultAddress), StringUtils.EMPTY, ErpAddressType.DELIVERY).get();
    Assert.assertThat(result, Matchers.is(defaultAddress));
  }

  @Test
  public void atAddressFilter_shouldReturnDefaultAddressIfNoBillingAddress() {
    Address defaultAddress = Address.builder().id("default1").addressTypeCode(ErpAddressType.DEFAULT.name()).build();
    Address deliveryAddress = Address.builder().id("delivery1").addressTypeCode(ErpAddressType.DELIVERY.name()).build();
    Address result = addressFilter
        .filterAddress(Arrays.asList(deliveryAddress, defaultAddress), StringUtils.EMPTY, ErpAddressType.INVOICE).get();
    Assert.assertThat(result, Matchers.is(defaultAddress));
  }

}
