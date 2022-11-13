package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.repo.api.DeliveryProfileRepository;
import com.sagag.eshop.service.converter.DeliveryProfileConverters;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.hazelcast.api.DeliveryProfileCacheService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeliveryProfileCacheServiceImpl extends CacheDataProcessor
    implements DeliveryProfileCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private DeliveryProfileRepository deliveryProfileRepo;

  @Override
  public boolean exists() {
    return !hazelcastInstance.getMap(defName()).isEmpty();
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available delivery profile from connect DB to Hazelcast instance");
    refreshCacheDeliveryProfiles(getAllCacheData(pageable -> deliveryProfileRepo.findAll(pageable)
        .map(DeliveryProfileConverters.deliveryProfileDtoConverter())));
  }

  private void refreshCacheDeliveryProfiles(List<DeliveryProfileDto> deliveryProfiles) {
    hazelcastInstance.getMap(defName()).evictAll();
    final IMap<Integer, DeliveryProfileDto> map = hazelcastInstance.getMap(defName());
    deliveryProfiles.parallelStream().forEach(p -> map.put(p.getId(), p));
  }

  @Override
  public List<DeliveryProfileDto> findAll() {
    IMap<String, DeliveryProfileDto> externalVendors = hazelcastInstance.getMap(defName());
    return externalVendors.values().stream().collect(Collectors.toList());
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }
}
