package com.sagag.services.incentive.builder.outlet;

import com.sagag.services.common.utils.HashUtils;
import com.sagag.services.incentive.builder.IOutletBuilder;
import com.sagag.services.incentive.config.IncentiveEndpointInfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.util.Assert;

@RequiredArgsConstructor
public class TechnomagOutletBuilder implements IOutletBuilder {

  @NonNull
  private IncentiveEndpointInfo endpointInfo;

  @NonNull
  private String lang;

  @NonNull
  private String email;

  @NonNull
  private String name;

  @NonNull
  private Long custNr;

  @Override
  public String buildOutlet() {
    Assert.hasText(lang, "lang must not empty");
    Assert.hasText(email, "email must not empty");
    Assert.hasText(name, "name must not empty");
    Assert.notNull(custNr, "custNr must not null");

    final String value = custNr.toString() + endpointInfo.getSecureKey();
    return new StringBuilder(endpointInfo.getUrl())
        .append("?name=").append(name)
        .append("&lang=").append(lang)
        .append("&email=").append(email)
        .append("&knr=") .append(custNr)
        .append("&key=").append(HashUtils.hashSHA1(value))
        .toString();

  }

}
