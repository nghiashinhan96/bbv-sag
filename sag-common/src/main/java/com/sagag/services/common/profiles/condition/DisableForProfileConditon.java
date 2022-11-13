package com.sagag.services.common.profiles.condition;

import com.sagag.services.common.profiles.DisableForProfile;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DisableForProfileConditon implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    MultiValueMap<String, Object> attrs =
        metadata.getAllAnnotationAttributes(DisableForProfile.class.getName());
    if (attrs == null) {
      return false;
    }

    final List<Object> profileObjectValues = attrs.get("value");
    final List<String> profileValues = profileObjectValues.stream()
        .flatMap(values -> Arrays.asList((String[]) values).stream())
        .collect(Collectors.toList());
    return !context.getEnvironment().acceptsProfiles(profileValues.toArray(new String[] {}));
  }

}
