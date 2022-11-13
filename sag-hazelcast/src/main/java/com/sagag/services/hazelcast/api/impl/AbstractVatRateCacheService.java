package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.sagag.eshop.service.api.VatRateService;
import com.sagag.services.hazelcast.api.VatRateCacheService;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractVatRateCacheService extends CacheDataProcessor
    implements VatRateCacheService {

  @Autowired
  protected HazelcastInstance hazelcastInstance;

  @Autowired
  protected VatRateService vatRateService;
}
