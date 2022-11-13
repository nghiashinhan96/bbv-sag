package com.sagag.services.oauth2.profiles;

import com.sagag.services.common.profiles.condition.AbstractFeatureCondition;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiPredicate;

public class OAuth2ExternalAuthenticatorCondition extends AbstractFeatureCondition {

  @Override
  public Class<?> featureAnnotation() {
    return OAuth2ExternalAuthenticatorMode.class;
  }

  @Override
  public String envKey() {
    return "country.config.oauth2-external-authenticator";
  }

  @Override
  public BiPredicate<Object, String> predicate() {
    return (value, envVal) -> {
      if (StringUtils.isBlank(envVal)) {
        return false;
      }
      return StringUtils.equalsIgnoreCase((String) value, StringUtils.trim(envVal));
    };
  }

}
