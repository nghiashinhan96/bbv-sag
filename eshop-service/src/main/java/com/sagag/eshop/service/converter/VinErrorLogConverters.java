package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.VinErrorLog;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.VinErrorLogDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class VinErrorLogConverters {

  public Function<VinErrorLogDto, VinErrorLog> vinErrorLogEntityConverter() {
    return (profile) -> SagBeanUtils.map(profile, VinErrorLog.class);
  }

}
