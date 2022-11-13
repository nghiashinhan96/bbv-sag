package com.sagag.services.elasticsearch.indices;

public interface EsIndexAliasMapper {

  /**
   * Maps the index with alias by key.
   *
   * @param keyAlias
   * @return the compatible alias with country and language
   */
  String mapAlias(String keyAlias);

}
