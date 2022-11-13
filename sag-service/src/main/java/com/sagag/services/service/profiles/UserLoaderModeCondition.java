package com.sagag.services.service.profiles;

import com.sagag.services.common.enums.country.UserLoaderMode;
import com.sagag.services.common.profiles.condition.AbstractFeatureCondition;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;

public class UserLoaderModeCondition extends AbstractFeatureCondition {

  @Override
  public Class<?> featureAnnotation() {
    return UserLoaderModeProfile.class;
  }

  @Override
  public String envKey() {
    return "country.config.user-loader-mode";
  }

  @Override
  public BiPredicate<Object, String> predicate() {
    return (value, envVal) -> {
      if (StringUtils.isBlank(envVal)) {
        return false;
      }
      return UserLoaderMode.class.cast(value) == UserLoaderMode.valueOf(envVal);
    };
  }

}
