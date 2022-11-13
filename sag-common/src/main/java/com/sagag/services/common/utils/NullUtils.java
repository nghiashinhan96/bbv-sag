package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NullUtils {

  public static String defaultEnum(Enum<?> e) {
    if (e == null) {
      return null;
    }
    return e.name();
  }

}
