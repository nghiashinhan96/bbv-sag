package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.VCollectionSearch;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.CollectionSearchResultDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class CollecitonConverters {

  public static Function<VCollectionSearch, CollectionSearchResultDto> collectionsConverter() {
    return col -> SagBeanUtils.map(col, CollectionSearchResultDto.class);
  }
}
