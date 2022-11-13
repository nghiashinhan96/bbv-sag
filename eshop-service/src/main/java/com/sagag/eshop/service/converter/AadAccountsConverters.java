package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.AadAccounts;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.AadAccountsSearchResultDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class AadAccountsConverters {

  private static AadAccountsSearchResultDto convert(AadAccounts aadAccount) {
    return SagBeanUtils.map(aadAccount, AadAccountsSearchResultDto.class);
  }

  public static Function<AadAccounts, AadAccountsSearchResultDto> toSearchResultItemDto() {
    return AadAccountsConverters::convert;
  }
}
