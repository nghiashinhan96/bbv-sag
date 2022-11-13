package com.sagag.services.elasticsearch.config.indices;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomElasticsearchAliasConfigProperties implements Serializable {

  private static final long serialVersionUID = 8657026086089805275L;

  private String alias;
  private String aliasPattern;
  private String defaultAlias;

}
