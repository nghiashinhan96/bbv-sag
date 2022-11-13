package com.sagag.services.haynespro.api.impl;

import com.sagag.services.haynespro.api.HaynesProService;
import com.sagag.services.haynespro.builder.HaynesProShopCartResponseBuilder;
import com.sagag.services.haynespro.client.HaynesProClient;
import com.sagag.services.haynespro.config.HaynesProProfile;
import com.sagag.services.haynespro.domain.RegisterVisitResult;
import com.sagag.services.haynespro.dto.HaynesProOptionDto;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;
import com.sagag.services.haynespro.enums.HaynesProOption;
import com.sagag.services.haynespro.request.HaynesProAccessUrlRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@HaynesProProfile
@Slf4j
public class HaynesProServiceImpl implements HaynesProService {

  @Autowired
  private HaynesProClient haynesProClient;

  @Override
  public List<HaynesProOptionDto> getHaynesProAccessOptions() {
    return Stream.of(HaynesProOption.values())
        .map(HaynesProOption::toDto).collect(Collectors.toList());
  }

  @Override
  public String getHaynesProAccessUrl(HaynesProAccessUrlRequest request) {
    log.debug("Returning HaynesPro Access Url = {}", request);
    final RegisterVisitResult result = haynesProClient.getHaynesProAccessUrl(request);
    return StringUtils.defaultString(result.getRedirectUrl());
  }

  @Override
  public Optional<HaynesProShoppingCart> getHaynesProShoppingCart(final String key,
      final BufferedReader reader) {
    log.debug("Handling call back HaynesPro by key = {}", key);
    if (reader == null || StringUtils.isBlank(key)) {
      log.warn("Can not process HaynesPro callback functional");
      return Optional.empty();
    }

    final HaynesProShoppingCart cart = new HaynesProShopCartResponseBuilder(reader).build();
    if (cart == null) {
      return Optional.empty();
    }
    return Optional.of(cart);
  }
}
