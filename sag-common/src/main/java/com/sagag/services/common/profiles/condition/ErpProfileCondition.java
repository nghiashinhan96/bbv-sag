package com.sagag.services.common.profiles.condition;

import com.sagag.services.common.enums.country.ErpType;
import com.sagag.services.common.profiles.ErpProfile;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class ErpProfileCondition extends AbstractFeatureCondition {

  @Override
  public Class<?> featureAnnotation() {
    return ErpProfile.class;
  }

  @Override
  public String envKey() {
    return "country.config.erp-type";
  }

  @Override
  public BiPredicate<Object, String> predicate() {
    return (values, envVal) -> {
      if (StringUtils.isBlank(envVal)) {
        return false;
      }
      return Stream.of((ErpType[]) values)
          .anyMatch(value -> ErpType.class.cast(value) == ErpType.valueOf(envVal));
    };
  }

}
