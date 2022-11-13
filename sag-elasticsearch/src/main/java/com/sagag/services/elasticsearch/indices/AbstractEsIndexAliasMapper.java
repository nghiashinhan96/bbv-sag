package com.sagag.services.elasticsearch.indices;

import com.sagag.services.common.country.CountryConfiguration;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.config.indices.CustomElasticsearchAliasConfigProperties;
import com.sagag.services.elasticsearch.config.indices.CustomElasticsearchConfigProperties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public abstract class AbstractEsIndexAliasMapper implements EsIndexAliasMapper, InitializingBean {

  private static final String INFO_MSG = "Initializing Elasticsearch index alias finder with "
    + "country code = {}, erp code = {} and \nindex alias map = {}";

  @Autowired
  private CountryConfiguration countryConfig;

  @Autowired
  private CustomElasticsearchConfigProperties customElasticsearchConfigProperties;

  @Getter
  private String erpShortCode;

  @Getter
  private String countryCode;

  @Getter
  private Map<String, CustomElasticsearchAliasConfigProperties> indexAliasMap;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.erpShortCode = countryConfig.getSupportedErpType().getShortCode();
    this.countryCode = countryConfig.getCode();
    this.indexAliasMap = customElasticsearchConfigProperties.getAliasMapper();
    log.info(INFO_MSG, countryCode, erpShortCode,
        SagJSONUtil.convertObjectToPrettyJson(indexAliasMap));
  }

  protected abstract Map<String, String> aliasMapValueBuilder(String erpShortCode, String country,
    String language);
}
