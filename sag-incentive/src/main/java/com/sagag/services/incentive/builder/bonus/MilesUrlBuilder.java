package com.sagag.services.incentive.builder.bonus;

import com.sagag.services.common.utils.HashUtils;
import com.sagag.services.incentive.builder.IncentiveUrlBuilder;
import com.sagag.services.incentive.config.IncentiveEndpointInfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.util.Assert;

@RequiredArgsConstructor
public class MilesUrlBuilder implements IncentiveUrlBuilder {

  private static final String SHOP_VALUE = "45";

  @NonNull
  private IncentiveEndpointInfo milesInfo;

  @NonNull
  private Long custNr;

  @Override
  public String buildUrl() {
    Assert.notNull(custNr, "custNr must not null");
    final String keyValues = milesInfo.getSecureKey() + SHOP_VALUE + custNr;
    return new StringBuilder(milesInfo.getUrl())
        .append("?user=").append(custNr)
        .append("&shop=").append(SHOP_VALUE)
        .append("&hash=").append(HashUtils.hashMD5(keyValues))
        .toString();
  }

}
