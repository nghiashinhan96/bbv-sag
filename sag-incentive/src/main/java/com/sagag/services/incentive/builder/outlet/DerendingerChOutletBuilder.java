package com.sagag.services.incentive.builder.outlet;

import com.sagag.services.common.utils.HashUtils;
import com.sagag.services.incentive.builder.IOutletBuilder;
import com.sagag.services.incentive.config.IncentiveEndpointInfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.util.Assert;

@RequiredArgsConstructor
public class DerendingerChOutletBuilder implements IOutletBuilder {

  @NonNull
  private IncentiveEndpointInfo endpointInfo;

  @NonNull
  private String lang;

  @NonNull
  private String email;

  @Override
  public String buildOutlet() {
    Assert.hasText(lang, "lang must not empty");
    Assert.hasText(email, "email must not empty");
    return new StringBuilder(endpointInfo.getUrl()).append('?')
        .append("c=").append(lang)
        .append("&b=").append(email)
        .append("&c=").append(HashUtils.hashSHA1(email + endpointInfo.getSecureKey()))
        .toString();
  }

}
