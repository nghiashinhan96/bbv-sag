package com.sagag.services.service.category;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.hazelcast.api.CategoryCacheService;
import com.sagag.services.hazelcast.api.impl.CacheDataProcessor;
import com.sagag.services.service.enums.CategoryType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class AbstractCategoryBuilderImplIT {

  private static final String VEHICLE_ID = "V107036M31403";

  @Autowired
  protected CategoryCacheService cateCacheService;

  @Autowired
  @Qualifier("categoryCacheServiceImpl")
  private CacheDataProcessor processor;

  protected CategoryItemCriteria criteria;

  protected SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_AT;
  protected Collection<CategoryDoc> categoriesOfVehicle;
  protected Map<String, CategoryDoc> allCategories;

  @Before
  public void setup() {
    if (!CollectionUtils.isEmpty(categoriesOfVehicle) && !MapUtils.isEmpty(allCategories)) {
      return;
    }
    LocaleContextHolder.setLocale(Locale.GERMAN);
    processor.refreshCacheAll();
    categoriesOfVehicle = cateCacheService.findCategoriesByVehicle(VEHICLE_ID);
    allCategories = cateCacheService.getAllCacheCategories();

    criteria = CategoryItemCriteria.builder().aff(affiliate).returnCUPIs(false)
        .externalUrls(Collections.emptyMap())
        .categoryType(CategoryType.ALL)
        .categoryTypes(CategoryType.availableValues().toArray(new CategoryType[] {}))
        .build();
  }

}
