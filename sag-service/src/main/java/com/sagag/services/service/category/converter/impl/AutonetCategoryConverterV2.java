package com.sagag.services.service.category.converter.impl;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.eshop.category.GenArtDto;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.service.category.converter.AbstractCategoryConverter;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

@Component
@AutonetProfile
public class AutonetCategoryConverterV2 extends AbstractCategoryConverter {

  @Override
  public Function<GenArt, GenArtDto> genArtConverter(SupportedAffiliate aff, String collection,
      boolean addCUPICode, Map<String, CachedBrandPriorityDto> brandPrioritiesMap) {
    return item -> convertGenArtDto(item, aff, addCUPICode, brandPrioritiesMap.get(item.getGaid()));
  }

  private static GenArtDto convertGenArtDto(GenArt genArtItem, SupportedAffiliate aff,
      boolean addCUPICode, CachedBrandPriorityDto brandPriority) {
    final GenArtDto genArtDto = cloneGenArtDto(genArtItem, addCUPICode);
    genArtDto.setBrands(Collections.emptyList());
    if (brandPriority != null && !CollectionUtils.isEmpty(brandPriority.getSorts())) {
      genArtDto.setSorts(brandPriority.getSorts());
    }
    return genArtDto;
  }

}
