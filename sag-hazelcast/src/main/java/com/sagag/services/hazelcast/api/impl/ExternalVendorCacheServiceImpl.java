package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.repo.api.ExternalVendorRepository;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.hazelcast.api.ExternalVendorCacheService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExternalVendorCacheServiceImpl extends CacheDataProcessor
    implements ExternalVendorCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Autowired
  private ExternalVendorRepository externalVendorRepo;

  @Override
  public boolean exists() {
    return !hazelcastInstance.getMap(defName()).isEmpty();
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available external vendors from connect DB to Hazelcast instance");
    refreshCacheExternalVendors(getAllCacheData(pageable -> externalVendorRepo.findAll(pageable)
        .map(item -> SagBeanUtils.map(item, ExternalVendorDto.class))));
  }

  private void refreshCacheExternalVendors(List<ExternalVendorDto> externalVendors) {
    hazelcastInstance.getMap(defName()).evictAll();
    final IMap<Integer, ExternalVendorDto> map = hazelcastInstance.getMap(defName());
    externalVendors.parallelStream().forEach(v -> map.put(v.getId(), v));
  }

  @Override
  public List<ExternalVendorDto> findAll() {
    IMap<String, ExternalVendorDto> externalVendors = hazelcastInstance.getMap(defName());
    return externalVendors.values().stream().collect(Collectors.toList());
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }
}
