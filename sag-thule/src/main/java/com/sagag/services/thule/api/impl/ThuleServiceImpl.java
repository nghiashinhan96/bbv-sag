package com.sagag.services.thule.api.impl;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.thule.api.ThuleService;
import com.sagag.services.thule.config.ThuleProfile;
import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.ThuleProperties;
import com.sagag.services.thule.extractor.BuyersGuideDataExtractor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ThuleProfile
@Slf4j
public class ThuleServiceImpl implements ThuleService {

  private static final String SALES_MODE_KEY_SUFFIX = "-c4s";

  @Autowired
  private BuyersGuideDataExtractor buyersGuideDataExtractor;

  @Autowired
  private ThuleProperties thuleProps;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public Optional<BuyersGuideData> addBuyersGuide(Map<String, String> formData) {
    log.info("Received Form Data = {}", SagJSONUtil.convertObjectToPrettyJson(formData));
    final Optional<BuyersGuideData> buyersGuideData = buyersGuideDataExtractor.apply(formData);
    buyersGuideData.ifPresent(data -> log.info("Thule Buyers Guide Data = {}",
        SagJSONUtil.convertObjectToPrettyJson(data)));
    return buyersGuideData;
  }

  @Override
  public Optional<String> findThuleDealerUrlByAffiliate(String affiliate, boolean isSalesMode,
      Locale locale) {
    if (StringUtils.isBlank(affiliate)
        || StringUtils.equalsIgnoreCase(SagConstants.SAG, affiliate)) {
      return Optional.empty();
    }
    final StringBuilder dealerIdKeyBuilder = new StringBuilder(affiliate);
    if (isSalesMode) {
      dealerIdKeyBuilder.append(SALES_MODE_KEY_SUFFIX);
    }

    final String dealerIdStr = MapUtils.emptyIfNull(thuleProps.getDealerIdMap())
        .get(dealerIdKeyBuilder.toString());
    if (StringUtils.isBlank(dealerIdStr)) {
      return Optional.empty();
    }

    final String country = localeContextHelper
        .findCountryByAffiliate(SupportedAffiliate.fromDescSafely(affiliate));

    return buildThuleDealerUri(thuleProps.getEndpoint(), dealerIdStr,
        localeContextHelper.defaultLocale(locale).getLanguage(), country);
  }

  private static Optional<String> buildThuleDealerUri(String endpointUrl, String dealerId,
      String language, String country) {
    final Map<String, String> valueMap = new HashMap<>();
    valueMap.put("dealerId", dealerId);
    valueMap.put("language", StringUtils.lowerCase(language));
    valueMap.put("country", StringUtils.lowerCase(country));
    return Optional.ofNullable(new StrSubstitutor(valueMap, "{", "}").replace(endpointUrl));
  }

}
