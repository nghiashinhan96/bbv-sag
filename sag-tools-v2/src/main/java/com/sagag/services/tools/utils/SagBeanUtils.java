package com.sagag.services.tools.utils;

import org.dozer.DozerBeanMapper;

public final class SagBeanUtils {

  private static final DozerBeanMapper DOZER_MAPPER = new DozerBeanMapper();

  private SagBeanUtils() {
  }

  public static <T> T map(Object source, Class<T> clazz) {
    return DOZER_MAPPER.map(source, clazz);
  }

}
