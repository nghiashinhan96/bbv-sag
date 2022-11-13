package com.sagag.services.tools.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * Utilities for environments.
 */
@UtilityClass
public final class EnvUtils {

  private static final String PRODUCTION_PATTERN = "prod";

  private static final String TESTING_PATTERN = "testing";

  public static boolean isProductionEnv(String profile) {
    return Stream.of(PRODUCTION_PATTERN, TESTING_PATTERN)
      .anyMatch(env -> StringUtils.equalsIgnoreCase(env, profile));
  }

}
