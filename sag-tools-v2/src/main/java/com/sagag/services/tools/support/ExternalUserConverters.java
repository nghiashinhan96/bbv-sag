package com.sagag.services.tools.support;


import com.sagag.services.tools.domain.mdm.ExternalUserDto;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.utils.SagBeanUtils;

import lombok.experimental.UtilityClass;

import org.springframework.core.convert.converter.Converter;

import java.util.function.Function;

/**
 * Utility provide some converters of external user.
 */
@UtilityClass
public final class ExternalUserConverters {

  public static Converter<ExternalUser, ExternalUserDto> defaultExternalUserConverter() {
    return entity -> SagBeanUtils.map(entity, ExternalUserDto.class);
  }

  public static Function<ExternalUser, ExternalUserDto> optionalExternalUserConverter() {
    return entity -> SagBeanUtils.map(entity, ExternalUserDto.class);
  }

}
