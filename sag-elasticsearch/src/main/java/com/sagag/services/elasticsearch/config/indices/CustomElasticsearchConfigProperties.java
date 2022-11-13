package com.sagag.services.elasticsearch.config.indices;

import lombok.Data;

import java.util.Map;

@Data
public class CustomElasticsearchConfigProperties {

  private Map<String, CustomElasticsearchAliasConfigProperties> aliasMapper;
}
