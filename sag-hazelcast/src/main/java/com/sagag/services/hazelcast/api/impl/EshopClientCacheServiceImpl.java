package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.eshop.service.api.EshopClientService;
import com.sagag.eshop.service.dto.client.EshopClientDto;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.hazelcast.api.EshopClientCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EshopClientCacheServiceImpl extends CacheDataProcessor
    implements EshopClientCacheService {

  @Autowired
  private EshopClientService eshopClientService;

  private HazelcastInstance hazelcastInstance;

  private final IMap<String, EshopClientDto> eshopClientMap;

  /**
   * Initializes the shopping cart manager.
   */
  @Autowired
  public EshopClientCacheServiceImpl(final HazelcastInstance hazelcast) {
    this.eshopClientMap = hazelcast.getMap(defName());
    this.hazelcastInstance = hazelcast;
  }

  @Override
  public Optional<EshopClientDto> findByClientId(String clientId) {
    if (StringUtils.isBlank(clientId)) {
      return Optional.empty();
    }

    final EshopClientDto clientDetail = this.eshopClientMap.get(clientId);
    if (clientDetail != null) {
      log.debug("Returning the eshop client info from cache = {}", clientDetail);
      return Optional.of(clientDetail);
    }

    final Optional<EshopClientDto> clientDetailOpt =
        eshopClientService.findActiveClientByClientName(clientId);
    clientDetailOpt.ifPresent(this::putEshopClientToIMap);
    return clientDetailOpt;
  }

  @Override
  public void refreshCacheAll() {
    final List<EshopClientDto> eshopClients = getAllCacheData(
        pageable -> eshopClientService.findAllActiveEshopClient(pageable),
        SagConstants.DEFAULT_PAGE_NUMBER);
    eshopClients.stream().forEach(this::putEshopClientToIMap);
  }

  private void putEshopClientToIMap(EshopClientDto detail) {
    log.debug("Putting to cache and returning the eshop client info from database = {}", detail);
    this.eshopClientMap.put(detail.getClientName(), detail);
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
