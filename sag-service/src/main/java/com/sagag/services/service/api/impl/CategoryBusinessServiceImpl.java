package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.VehicleNotFoundException;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.GenArt;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import com.sagag.services.hazelcast.api.BrandPriorityCacheService;
import com.sagag.services.hazelcast.api.CategoryCacheService;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.service.api.CategoryBusinessService;
import com.sagag.services.service.category.CategoryItemCriteria;
import com.sagag.services.service.category.QuickClickCategoryBuilder;
import com.sagag.services.service.category.SearchResultCategoryBuilder;
import com.sagag.services.service.category.TreeCategoryBuilder;
import com.sagag.services.service.enums.CategoryType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryBusinessServiceImpl implements CategoryBusinessService {

  @Autowired
  private QuickClickCategoryBuilder quickClickCategoryBuilder;

  @Autowired
  private TreeCategoryBuilder treeCategoryBuilder;

  @Autowired
  private SearchResultCategoryBuilder searchResultCategoryBuilder;

  @Autowired
  private CategoryCacheService catCacheService;

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Autowired
  private BrandPriorityCacheService brandPriorityCacheService;

  @Autowired
  private VehicleSearchService vehicleSearchService;

  @Override
  @LogExecutionTime
  public Map<CategoryType, List<CategoryItem>> getCategoriesByVehicleIdAndVehicleType(UserInfo user,
      String vehicleId, boolean addCupiCode) {
    Assert.hasText(vehicleId, "The vehicle id must not be empty");

    final String vehicleClass = findVehicleClassFromVehicleId(vehicleId);

    final Collection<CategoryDoc> categoriesOfVehicle =
        catCacheService.findCategoriesByVehicle(vehicleId);
    List<CategoryDoc> filteredCategoriesOfVehicleByVehicleClass =
        CollectionUtils.emptyIfNull(categoriesOfVehicle).stream()
            .filter(cate -> StringUtils.equalsIgnoreCase(cate.getVehicleClass(), vehicleClass))
            .collect(Collectors.toList());

    final Map<String, CategoryDoc> allCategories = catCacheService.getAllCacheCategories();
    if (CollectionUtils.isEmpty(filteredCategoriesOfVehicleByVehicleClass) || MapUtils.isEmpty(allCategories)) {
      return Collections.emptyMap();
    }

    Map<String, CategoryDoc> filteredCategories =
        filterCategoriesByVehicleClass(allCategories, vehicleClass);

    final List<String> gaidListOfVehicle = filteredCategoriesOfVehicleByVehicleClass.stream()
        .flatMap(c -> c.getGenarts().stream()).map(GenArt::getGaid).collect(Collectors.toList());

    final Map<String, CachedBrandPriorityDto> brandSortingDtos =
        brandPriorityCacheService.findCachedBrandPriority(gaidListOfVehicle);

    final CategoryItemCriteria criteria = initCateItemCriteria(user, addCupiCode, brandSortingDtos);

    final Map<CategoryType, List<CategoryItem>> categories = treeCategoryBuilder
        .buildCategories(filteredCategoriesOfVehicleByVehicleClass, filteredCategories, criteria);

    return Collections.unmodifiableMap(categories);
  }

  @Override
  @LogExecutionTime
  public List<List<CategoryItem>> getQuickClickCategoriesByVehicleId(UserInfo user,
      String vehicleId, boolean addCupiCode) {

    final String vehicleClass = findVehicleClassFromVehicleId(vehicleId);
    final Collection<CategoryDoc> categoriesOfVehicle =
        catCacheService.findCategoriesByVehicle(vehicleId);
    List<CategoryDoc> filteredCategoriesOfVehicleByVehicleClass =
        CollectionUtils.emptyIfNull(categoriesOfVehicle).stream()
            .filter(cate -> StringUtils.equalsIgnoreCase(cate.getVehicleClass(), vehicleClass))
            .collect(Collectors.toList());

    final Map<String, CategoryDoc> allCategories = catCacheService.getAllCacheCategories();
    if (CollectionUtils.isEmpty(filteredCategoriesOfVehicleByVehicleClass)
        || MapUtils.isEmpty(allCategories)) {
      return Collections.emptyList();
    }

    Map<String, CategoryDoc> filteredCategories =
        filterCategoriesByVehicleClass(allCategories, vehicleClass);

    final CategoryItemCriteria criteria =
        initCateItemCriteria(user, addCupiCode, Collections.emptyMap());

    final List<List<CategoryItem>> categories = quickClickCategoryBuilder
        .buildCategories(filteredCategoriesOfVehicleByVehicleClass, filteredCategories, criteria);

    return Collections.unmodifiableList(categories);
  }

  @Override
  public Map<String, String> searchCategoriesByFreeText(UserInfo user, String vehicleId,
      boolean addCupiCode, String freetext) {
    final Page<CategoryDoc> searchedCategoriesPage =
        catCacheService.searchCategoriesByLeafText(freetext);
    if (!searchedCategoriesPage.hasContent()) {
      return Collections.emptyMap();
    }

    final Collection<CategoryDoc> categoriesOfVehicle =
        catCacheService.findCategoriesByVehicle(vehicleId);

    final Map<String, CategoryDoc> allCategories = catCacheService.getAllCacheCategories();

    if (CollectionUtils.isEmpty(categoriesOfVehicle) || MapUtils.isEmpty(allCategories)) {
      return Collections.emptyMap();
    }

    final CategoryItemCriteria criteria = initCateItemCriteria(user, addCupiCode,
        Collections.emptyMap());

    final Map<String, String> categories =
        searchResultCategoryBuilder.buildCategories(categoriesOfVehicle, allCategories,
            searchedCategoriesPage, criteria);

    return Collections.unmodifiableMap(categories);
  }

  private static CategoryItemCriteria initCateItemCriteria(UserInfo user, boolean addCupiCode,
      final Map<String, CachedBrandPriorityDto> brandSortingDtos) {
    return CategoryItemCriteria.builder().aff(user.getSupportedAffiliate()).returnCUPIs(addCupiCode)
        .externalUrls(user.getSettings().getExternalUrls()).brandSortingMap(brandSortingDtos)
        .categoryType(CategoryType.ALL)
        .categoryTypes(CategoryType.availableValues().toArray(new CategoryType[] {}))
        .collection(user.getCollectionName()).build();
  }

  @Override
  public Map<String, List<ArticleFilterItem>> searchArticlesByFilter(UserInfo user,
      ArticleFilterRequest request, Pageable pageable) {
    Assert.hasText(request.getFilterMode(), "The given filter mode must not be empty");
    final ArticleFilteringResponseDto response =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, pageable);
    return Collections.unmodifiableMap(response.getFilters());
  }

  private Map<String, CategoryDoc> filterCategoriesByVehicleClass(
      final Map<String, CategoryDoc> allCategories, final String vehicleClass) {
    Map<String, CategoryDoc> filteredCategories = null;
    if (StringUtils.isNotEmpty(vehicleClass)) {
      filteredCategories = allCategories.entrySet().stream()
          .filter(
              cate -> StringUtils.equalsIgnoreCase(cate.getValue().getVehicleClass(), vehicleClass))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    } else {
      filteredCategories = allCategories;
    }
    return filteredCategories;
  }

  private String findVehicleClassFromVehicleId(String vehicleId) {
    //TODO This can be improved by passing the vehicle class from the consumer (FE) for performance purpose
    final VehicleDoc vehicleOpt = vehicleSearchService.searchVehicleByVehId(vehicleId)
        .orElseThrow(VehicleNotFoundException::new);
    final String vehicleClass = vehicleOpt.getVehicleClass();
    return vehicleClass;
  }


}
