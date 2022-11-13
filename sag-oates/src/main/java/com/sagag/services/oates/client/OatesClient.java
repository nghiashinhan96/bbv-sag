package com.sagag.services.oates.client;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.oates.config.OatesProfile;
import com.sagag.services.oates.domain.OatesEquipmentProducts;
import com.sagag.services.oates.domain.OatesRecommendVehicles;

@Component
@OatesProfile
public class OatesClient {

  private static final String DF_VERSION = "2";

  private static final String DF_VRM_TYPE = "eur:ktype:chatham";

  private static final String API_VRM_SEARCH = "/vrm_search?q=${search_query}&token=${token}"
      + "&version=${version}&vrm_type=${vrm_type}&language=${lang}";

  private static final String API_EQUIPMENT = "${href}?token=${token}&language=${lang}";

  @Value("${external.webservice.oates.endpoint}")
  private String url;

  @Value("${external.webservice.oates.token}")
  private String token;

  @Autowired
  private OatesExchangeClient oatesExchangeClient;

  /**
   * Returns the recommend OATES vehicles by kType.
   *
   * <pre>
   * VRM Search Resource
   * The VRM Search resource allows applications to search equipment using a country-specific
   * vehicle registration mark.
   * </pre>
   *
   * @param kType
   * @param version
   * @param vrmType
   * @return the object of {@link OatesRecommendVehicles}
   */
  public OatesRecommendVehicles getOatesRecommendVehicles(String kType, String version,
      String vrmType) {
    Assert.hasText(kType, "The given kType must not be empty");
    final Map<String, String> valueMap = new HashMap<>();
    valueMap.put("search_query", kType);
    valueMap.put("version", StringUtils.defaultIfBlank(vrmType, DF_VERSION));
    valueMap.put("vrm_type", StringUtils.defaultIfBlank(vrmType, DF_VRM_TYPE));

    return oatesExchangeClient.exchange(toApiUrl(url, API_VRM_SEARCH, token, valueMap),
        HttpMethod.GET, OatesRecommendVehicles.class).getBody();
  }

  /**
   *
   * Returns the OATES equipment products by href string.
   *
   * <pre>
   * Equipment Resource
   * The equipment resource provides applications with detailed equipment information, including
   * lubricant recommendations.
   * In general, applications should not construct URLs under the equipment resource. Other
   * resources such as browse, search and vrm_search should be queried to find equipment.
   * </pre>
   * @param href
   * @return the object of {@link OatesEquipmentProducts}
   */
  public OatesEquipmentProducts getOatesRecommendProducts(String href) {
    Assert.hasText(href, "The given href string must not be empty");
    final Map<String, String> valueMap = new HashMap<>();
    valueMap.put("href", href);
    return oatesExchangeClient.exchange(toApiUrl(url, API_EQUIPMENT, token, valueMap),
        HttpMethod.GET, OatesEquipmentProducts.class).getBody();
  }

  private static String toApiUrl(String url, String api, String token,
      Map<String, String> valueMap) {
    Assert.hasText(url, "The given endpoint must not be empty");
    Assert.hasText(api, "The given api method must not be emoty");
    Assert.hasText(token, "The given token must not be empty");
    if (MapUtils.isEmpty(valueMap)) {
      valueMap = new HashMap<>();
    }
    valueMap.put("token", token);
    valueMap.put("lang", getDefaultLangForOatesPartner(
        LocaleContextHolder.getLocale().getLanguage()));
    return StrSubstitutor.replace(url + api, valueMap);
  }

  /**
   * Returns default language send to OATES partner
   * Note: handling some special case from #4384
   *
   */
  private static String getDefaultLangForOatesPartner(String language) {
    if (StringUtils.isBlank(language)
        || StringUtils.equalsIgnoreCase(Locale.ENGLISH.getLanguage(), language)) {
      return Locale.UK.toString();
    }
    return language;
  }

}
