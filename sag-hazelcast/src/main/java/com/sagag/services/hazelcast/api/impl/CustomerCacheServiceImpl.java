package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.CachedCustomerInfo;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation class for customer cache service.
 */
@Service
@Slf4j
public class CustomerCacheServiceImpl implements CustomerCacheService {

  @Autowired
  private CustomerExternalService customerExtService;

  @Autowired
  private HazelcastInstance cacheInstance;

  @Override
  public List<Address> getCachedCustomerAddresses(String customerNr, String companyName) {
    final CachedCustomerInfo cachedCustInfo = get(customerNr);
    if (cachedCustInfo == null) {
      log.warn("Not found customer info from cache data with custNr = {} - compName = {}",
          customerNr, companyName);
      return Collections.emptyList();
    }
    List<Address> addresses = cachedCustInfo.getCustomerAddresses();

    if (CollectionUtils.isNotEmpty(addresses)) {
      log.debug("Return cached customer addresses {}", addresses);
      return addresses;
    }

    final List<Address> customerAddresses = customerExtService
        .searchCustomerAddresses(companyName, customerNr);
    if (CollectionUtils.isEmpty(customerAddresses)) {
      throw new IllegalStateException();
    }
    log.debug("Customer addresses after ERP hit is {}", customerAddresses);
    put(CachedCustomerInfo.builder().customer(cachedCustInfo.getCustomer()).customerAddresses(customerAddresses).build());
    return customerAddresses;
  }

  @Override
  public Optional<Customer> getCachedCustomer(String customerNr, String companyName) {
    Assert.notNull(customerNr, "The given customerNr must not be null");
    Assert.notNull(companyName, "The given companyName must not be null");

    final CachedCustomerInfo customerInfo = get(customerNr);
    if (customerInfo != null) {
      log.debug("Return cached customer {}", customerInfo.getCustomer());
      return Optional.ofNullable(customerInfo.getCustomer());
    }

    final CustomerInfo updatedCustomerInfo = customerExtService
        .getActiveCustomerInfo(companyName, customerNr)
        .orElseThrow(IllegalArgumentException::new);
    final Customer customer = updatedCustomerInfo.getCustomer();
    if (!customer.isActive()) {
      throw AxCustomerException.buildBlockedCustomerException(customer.getBlockReason());
    }
    log.debug("Customer after ERP hit", customer);
    put(CachedCustomerInfo.builder().customer(customer).build());
    return Optional.of(customer);
  }

  @Override
  public void clearCache(String customerNr) {

    final IMap<String, CachedCustomerInfo> custMap = getCustomerInfoHazelcastInstanceMap();
    if (Objects.isNull(custMap)) {
      return;
    }
    custMap.remove(customerNr);
  }

  /**
   * Returns cached customer info from customer number.
   *
   * @param customerNr the customer number
   * @return the current customer logging info
   */
  private CachedCustomerInfo get(String customerNr) {
    // find the way to init map as one
    final IMap<String, CachedCustomerInfo> custMap = getCustomerInfoHazelcastInstanceMap();
    if (MapUtils.isEmpty(custMap)) {
      return null;
    }
    return custMap.get(customerNr);
  }

  private void put(CachedCustomerInfo customerInfo) {
    Asserts.notNull(customerInfo.getCustNr(), "Customer Nr must be not null");
    final IMap<String, CachedCustomerInfo> custMap = getCustomerInfoHazelcastInstanceMap();
    if (Objects.isNull(custMap)) {
      return;
    }
    custMap.set(String.valueOf(customerInfo.getCustNr()), customerInfo);
  }

  private IMap<String, CachedCustomerInfo> getCustomerInfoHazelcastInstanceMap() {
    return cacheInstance.getMap(HazelcastMaps.SESSION_CUSTOMER_MAP.name());
  }

}
