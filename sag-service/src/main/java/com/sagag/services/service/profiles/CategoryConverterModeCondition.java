package com.sagag.services.service.profiles;

import com.sagag.services.common.profiles.condition.AbstractFeatureCondition;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;

public class CategoryConverterModeCondition extends AbstractFeatureCondition {

  @Override
  public Class<?> featureAnnotation() {
    return CategoryConverterMode.class;
  }

  @Override
  public String envKey() {
    return "country.config.category-converter";
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
