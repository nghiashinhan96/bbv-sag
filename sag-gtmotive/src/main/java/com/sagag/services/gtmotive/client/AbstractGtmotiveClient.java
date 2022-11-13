package com.sagag.services.gtmotive.client;

import com.sagag.services.gtmotive.domain.GtmotiveAccounts;
import com.sagag.services.gtmotive.domain.GtmotiveProfileDto;
import com.sagag.services.gtmotive.profile.GtmotiveProfileLoader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractGtmotiveClient {

  protected static final List<Charset> DF_ACCEPT_CHARSETS =
      Collections.unmodifiableList(Arrays.asList(StandardCharsets.UTF_8));

  @Autowired
  protected RestTemplate restTemplate;

  @Autowired
  protected GtmotiveExchangeClient exchangeClient;

  @Autowired
  private List<GtmotiveProfileLoader> profileLoaders;

  protected GtmotiveProfileDto getProfile(final Locale locale, final GtmotiveAccounts accounts) {
    Assert.notNull(accounts, "The list of accounts must not be null");
    return profileLoaders.stream()
        .filter(loader -> loader.locale().equals(locale))
        .map(loader -> loader.getProfile(accounts)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("User language was not supported"));

  }
}
