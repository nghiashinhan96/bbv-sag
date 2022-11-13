package com.sagag.services.elasticsearch.indices.impl;

import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractEsIndexAliasMapperImplIT {

  @Autowired
  private DefaultEsIndexAliasMapperImpl mapper;

  protected void testMapAliasByCountryAndLanguage(Map<String, String> actualAndExpectedMap) {
    actualAndExpectedMap.forEach((key, expected) -> Assert.assertThat(
      mapper.mapAlias(key), Matchers.equalTo(expected)));
  }

}
