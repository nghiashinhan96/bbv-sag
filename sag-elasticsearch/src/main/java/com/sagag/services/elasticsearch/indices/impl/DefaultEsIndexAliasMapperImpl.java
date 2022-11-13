package com.sagag.services.elasticsearch.indices.impl;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.elasticsearch.config.indices.CustomElasticsearchAliasConfigProperties;
import com.sagag.services.elasticsearch.indices.AbstractEsIndexAliasMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class DefaultEsIndexAliasMapperImpl extends AbstractEsIndexAliasMapper {

  private static final String DELIMITER_OF_ALIAS_NAME = SagConstants.UNDERSCORE;

  private static final String PREFIX = "(";

  private static final String SUFFIX = ")";

  private static final String KEY_ERP_SHORT_CODE = "erp";

  private static final String KEY_LANGUAGE_CODE = "lc";

  private static final String KEY_COUNTRY_CODE ="cc";

  @Override
  public String mapAlias(String keyAlias) {
    log.debug("Finding alias by key alias = {}", keyAlias);
    Assert.hasText(keyAlias, "The given key must not be null");
    final CustomElasticsearchAliasConfigProperties aliasDto = this.getIndexAliasMap().get(keyAlias);
    if (aliasDto == null) {
      return keyAlias;
    }

    final String pattern =
      StringUtils.defaultIfBlank(aliasDto.getAliasPattern(), aliasDto.getDefaultAlias());
    if (StringUtils.isBlank(pattern)) {
      return keyAlias;
    }
    final Map<String, String> valueMap = aliasMapValueBuilder(this.getErpShortCode(),
      this.getCountryCode(), LocaleContextHolder.getLocale().getLanguage());
    final String aliasName = StringUtils.stripStart(
      StrSubstitutor.replace(pattern, valueMap, PREFIX, SUFFIX), DELIMITER_OF_ALIAS_NAME);
    log.debug("Found alias name = {}", aliasName);
    return aliasName;
  }

  @Override
  protected Map<String, String> aliasMapValueBuilder(String erpShortCode, String country,
    String language) {
    log.debug("Builds map value alias builder with erp = {} - country = {}, language = {}",
      erpShortCode, country, language);
    final Map<String, String> valueMap = new HashMap<>();
    valueMap.put(KEY_ERP_SHORT_CODE, StringUtils.lowerCase(erpShortCode));
    valueMap.put(KEY_LANGUAGE_CODE, StringUtils.lowerCase(language));
    valueMap.put(KEY_COUNTRY_CODE, StringUtils.lowerCase(country));
    return valueMap;
  }
}
