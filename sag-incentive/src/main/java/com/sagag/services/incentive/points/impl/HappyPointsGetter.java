package com.sagag.services.incentive.points.impl;

import com.sagag.services.incentive.config.IncentiveProfile;
import com.sagag.services.incentive.config.IncentiveProperties;
import com.sagag.services.incentive.points.IncentivePointsGetter;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@IncentiveProfile
@Slf4j
public class HappyPointsGetter implements IncentivePointsGetter {

  @Autowired
  private IncentiveProperties incentiveProps;

  @Autowired
  @Qualifier("incentiveRestTemplate")
  private RestTemplate restTemplate;

  @Override
  public Long get(String custNr) {
    log.debug("Getting happy points by customer = {}", custNr);
    final String url = String.format(incentiveProps.getHappyPoints().getUrl(), custNr);
    log.debug("The happy points by customer number = {}", url);
    return restTemplate.getForEntity(url, Long.class).getBody();
  }

}
