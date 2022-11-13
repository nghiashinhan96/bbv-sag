package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.CustomerSearchResultItemDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class OrganisationConverters {

  public static Function<Organisation, OrganisationDto> organisationConverter() {
    return org -> SagBeanUtils.map(org, OrganisationDto.class);
  }

  public static Function<Organisation, CustomerSearchResultItemDto> customerSearchConverter() {
    return org -> CustomerSearchResultItemDto.builder().affiliate(org.getShortname())
        .customerNr(org.getOrgCode()).companyName(org.getName()).build();
  }

}
