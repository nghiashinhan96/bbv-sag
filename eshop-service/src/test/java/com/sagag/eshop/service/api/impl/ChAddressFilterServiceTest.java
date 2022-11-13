package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.impl.address.ChAddressFilterImpl;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.domain.sag.erp.Address;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
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
public class ChAddressFilterServiceTest {

  @InjectMocks
  private ChAddressFilterImpl addressFilter;

  @Test
  public void chAddressFilter_shouldReturnOneElement() {
    List<Address> addressList = IntStream.range(0, 10).mapToObj(
        i -> Address.builder().addressTypeCode(i == 1 ? ErpAddressType.INVOICE.name() : ErpAddressType.DELIVERY.name())
            .build())
        .collect(Collectors.toList());
    List<Address> result = addressFilter.getBillingAddresses(addressList);
    Assert.assertThat(result, Matchers.hasSize(1));
  }

  @Test
  public void chAddressFilter_shouldReturnDefaultAddress() {
    Address invoiceAddress = Address.builder().id("invoice1").addressTypeCode(ErpAddressType.INVOICE.name()).build();
    Address defaultAddress = Address.builder().id("default1").addressTypeCode(ErpAddressType.DEFAULT.name()).build();
    Address deliveryAddress = Address.builder().id("delivery1").addressTypeCode(ErpAddressType.DELIVERY.name()).build();
    Address result =
        addressFilter.getDefaultAddress(Arrays.asList(invoiceAddress, defaultAddress, deliveryAddress)).get();
    Assert.assertThat(result, Matchers.is(defaultAddress));
  }

  @Test
  public void chAddressFilter_shouldReturnDefaultAddressIfNoBillingAddress() {
    Address defaultAddress = Address.builder().id("default1").addressTypeCode(ErpAddressType.DEFAULT.name()).build();
    Address deliveryAddress = Address.builder().id("delivery1").addressTypeCode(ErpAddressType.DELIVERY.name()).build();
    List<Address> result = addressFilter.getBillingAddresses(Arrays.asList(defaultAddress, deliveryAddress));
    Assert.assertThat(result, Matchers.hasSize(1));
    Assert.assertThat(result.get(0), Matchers.is(defaultAddress));
  }

  @Test
  public void chAddressFilter_shouldReturnDefaultAddressIfNoDeliveryAddress() {
    Address invoiceAddress = Address.builder().id("invoice1").addressTypeCode(ErpAddressType.INVOICE.name()).build();
    Address defaultAddress = Address.builder().id("default1").addressTypeCode(ErpAddressType.DEFAULT.name()).build();
    Address result = addressFilter
        .filterAddress(Arrays.asList(invoiceAddress, defaultAddress), StringUtils.EMPTY, ErpAddressType.DELIVERY).get();
    Assert.assertThat(result, Matchers.is(defaultAddress));
  }

  @Test(expected = IllegalStateException.class)
  public void chAddressFilter_expectIllegalStateException() {
    addressFilter
        .filterAddress(Lists.emptyList(), StringUtils.EMPTY, null).get();
  }

}
