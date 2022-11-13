package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.ExternalUser;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.ExternalUserDto;

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
