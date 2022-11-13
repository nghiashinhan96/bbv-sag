package com.sagag.services.elasticsearch.indices;

import org.springframework.util.Assert;

public interface IndexAliasKey {

  String keyAlias();

  EsIndexAliasMapper aliasMapper();

  default String index() {
    Assert.notNull(aliasMapper(), "The given alias mapper must not be null");
    return aliasMapper().mapAlias(keyAlias());
  }
}
