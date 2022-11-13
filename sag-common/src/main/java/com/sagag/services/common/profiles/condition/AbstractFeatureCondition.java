package com.sagag.services.common.profiles.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.function.BiPredicate;

public abstract class AbstractFeatureCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    MultiValueMap<String, Object> attrs =
        metadata.getAllAnnotationAttributes(featureAnnotation().getName());
    if (attrs == null) {
      return true;
    }
    final String envVal = context.getEnvironment().getProperty(envKey());
    for (Object value : attrs.get("value")) {
      if (value != null) {
        return predicate().test(value, envVal);
      }
    }
    return false;
  }

  /**
   * Returns the feature annotation.
   *
   */
  public abstract Class<?> featureAnnotation();

  /**
   * Returns the environment key need set value.
   *
   */
  public abstract String envKey();

  /**
   * Returns the condition to verify.
   *
   */
  public abstract BiPredicate<Object, String> predicate();

}
