package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.api.DeliveryProfileService;
import com.sagag.eshop.service.dto.CsvDeliveryProfileDto;
import com.sagag.eshop.service.exception.DeliveryProfileValidationException;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.service.api.DeliveryProfileBusinessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryProfileBusinessServiceImpl implements DeliveryProfileBusinessService {

  @Autowired
  private DeliveryProfileService deliveryProfileService;

  @Autowired
  @Qualifier("deliveryProfileCacheServiceImpl")
  private CacheDataProcessor processor;

  @Override
  @Transactional
  public void importAndRefreshCacheDeliveryProfile(List<CsvDeliveryProfileDto> csvDeliveryProfiles)
      throws DeliveryProfileValidationException {
    deliveryProfileService.replaceDeliveryProfilesByCsv(csvDeliveryProfiles);
    this.refreshCacheDeliveryProfile();
  }

  @Override
  public void refreshCacheDeliveryProfile() {
    processor.refreshCacheAll();
  }
}
