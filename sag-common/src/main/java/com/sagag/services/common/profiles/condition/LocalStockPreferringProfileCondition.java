package com.sagag.services.common.profiles.condition;

import com.sagag.services.common.profiles.LocalStockPreferringProfile;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;

public class LocalStockPreferringProfileCondition extends AbstractFeatureCondition {

  @Override
  public Class<?> featureAnnotation() {
    return LocalStockPreferringProfile.class;
  }

  @Override
  public String envKey() {
    return "country.config.enable-local-stock-availability-preferring";
  }

  @Override
  public BiPredicate<Object, String> predicate() {
    return (value, envVal) -> {
      if (StringUtils.isBlank(envVal)) {
        return false;
      }
      return BooleanUtils.toBoolean(envVal) == Boolean.class.cast(value);
    };
  }

}
