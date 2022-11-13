package com.sagag.services.service.category.converter.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.category.BrandDto;
import com.sagag.services.domain.eshop.category.GenArtDto;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.service.category.converter.AbstractCategoryConverter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCategoryConverterV2 extends AbstractCategoryConverter {

  @Override
  public Function<GenArt, GenArtDto> genArtConverter(SupportedAffiliate aff, String collection,
      boolean addCUPICode, Map<String, CachedBrandPriorityDto> brandPrioritiesMap) {
    return item -> convertGenArtDto(item, aff, collection, addCUPICode,
        brandPrioritiesMap.get(item.getGaid()));
  }

  private static GenArtDto convertGenArtDto(GenArt genArtItem, SupportedAffiliate aff,
      String collection, boolean addCUPICode, CachedBrandPriorityDto brandPriority) {
    final GenArtDto genArtDto = cloneGenArtDto(genArtItem, addCUPICode);
    if (brandPriority == null || CollectionUtils.isEmpty(brandPriority.getBrands())) {
      return genArtDto;
    }
    final List<BrandDto> brandsByCollection = brandPriority.getBrands().stream()
        .filter(b -> !StringUtils.isBlank(b.getCollection())
            && StringUtils.equalsIgnoreCase(collection, b.getCollection()))
        .collect(Collectors.toList());
    final List<BrandDto> brandsByAffiliate = brandPriority.getBrands().stream()
        .filter(b -> !StringUtils.isBlank(b.getAffiliate())
            && StringUtils.equalsIgnoreCase(aff.getEsShortName(), b.getAffiliate()))
        .collect(Collectors.toList());
    genArtDto.setBrands(ListUtils.union(brandsByCollection, brandsByAffiliate));
    genArtDto.setSorts(Collections.emptyList());
    return genArtDto;
  }

}
