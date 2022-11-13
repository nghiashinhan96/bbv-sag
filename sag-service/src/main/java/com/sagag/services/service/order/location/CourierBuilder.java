package com.sagag.services.service.order.location;

import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.eshop.dto.CourierDto;
import com.sagag.services.hazelcast.api.CourierCacheService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@SbProfile
public class CourierBuilder {

  @Autowired
  private CourierCacheService courierCacheService;

  public List<CourierDto> buildCourierServices(String companyName) {
    return CollectionUtils.emptyIfNull(courierCacheService.getCachedCouriers(companyName)).stream()
        .map(CourierDto::new).collect(Collectors.toList());
  }
}
