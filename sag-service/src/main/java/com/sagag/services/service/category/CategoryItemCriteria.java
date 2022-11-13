package com.sagag.services.service.category;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.service.enums.CategoryType;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class CategoryItemCriteria {

  private SupportedAffiliate aff;

  private String collection;

  private boolean returnCUPIs;

  private Map<String, String> externalUrls;

  private List<String> oilGaIds;

  private CategoryType categoryType;

  private CategoryType[] categoryTypes; // For tree category builder.

  private Map<String, CachedBrandPriorityDto> brandSortingMap;

  public boolean isIgnoreOpenVal() {
    if (categoryType == null) {
      return false;
    }
    return categoryType.isIgnoredOpen();
  }

}
