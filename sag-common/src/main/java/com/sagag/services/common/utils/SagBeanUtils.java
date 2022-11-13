package com.sagag.services.common.utils;

import lombok.experimental.UtilityClass;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeanUtils;

@UtilityClass
public final class SagBeanUtils {

  private static final DozerBeanMapper DOZER_MAPPER = new DozerBeanMapper();

  public static <T> T map(Object source, Class<T> clazz) {
    return DOZER_MAPPER.map(source, clazz);
  }

  public static void copyProperties(Object source, Object target) {
    BeanUtils.copyProperties(source, target);
  }

}
