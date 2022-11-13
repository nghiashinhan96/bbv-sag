package com.sagag.services.elasticsearch.profiles;

import com.sagag.services.common.profiles.condition.AbstractFeatureCondition;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;

public class EsExternalArticleServiceModeCondition extends AbstractFeatureCondition {

  @Override
  public Class<?> featureAnnotation() {
    return EsExternalArticleServiceMode.class;
  }

  @Override
  public String envKey() {
    return "country.config.es-external-article-service";
  }

  @Override
  public BiPredicate<Object, String> predicate() {
    return (value, envVal) -> {
      if (StringUtils.isBlank(envVal)) {
        return false;
      }
      return StringUtils.equalsIgnoreCase((String) value, envVal);
    };
  }

}
