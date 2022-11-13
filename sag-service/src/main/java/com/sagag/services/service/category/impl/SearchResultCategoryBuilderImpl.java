package com.sagag.services.service.category.impl;

import com.google.common.collect.Lists;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.service.category.CategoryItemCriteria;
import com.sagag.services.service.category.SearchResultCategoryBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SearchResultCategoryBuilderImpl extends AbstractCategoryBuilder
    implements SearchResultCategoryBuilder {

  private static final char RIGHT_ARROW = '>';

  private static final String CATEGORY_NODE_PATH_SEPARATOR = " > ";

  @Override
  public Map<String, String> buildCategories(
      Collection<CategoryDoc> categoriesOfVehicle, Map<String, CategoryDoc> allCategories,
      CategoryItemCriteria criteria) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Map<String, String> buildCategories(Collection<CategoryDoc> categoriesOfVehicle,
      Map<String, CategoryDoc> allCategories, Page<CategoryDoc> searchedCategoriesPage,
      CategoryItemCriteria criteria) {
    if (MapUtils.isEmpty(allCategories) || CollectionUtils.isEmpty(categoriesOfVehicle)
        || Objects.isNull(searchedCategoriesPage) || !searchedCategoriesPage.hasContent()) {
      return Collections.emptyMap();
    }

    final List<String> searchedLeafIds = searchedCategoriesPage.getContent().stream()
        .map(CategoryDoc::getId).collect(Collectors.toList());

    final Map<String, CategoryItem> cateItems =
        categoryTreeCombinator.combine(categoriesOfVehicle, allCategories, criteria);

    final Map<String, String> searchedCategories = new HashMap<>();
    String cateId;
    CategoryItem cateItem;
    StringBuilder cateDescBuilder;
    Map<String, String> searchedNestedCategoriesMap;
    for (Entry<String, CategoryItem> entry : cateItems.entrySet()) {
      cateId = entry.getKey();
      cateItem = entry.getValue();
      if (!searchedLeafIds.contains(cateId) || !cateItem.isValidForSearching()) {
        continue;
      }

      List<String> path = Lists.newArrayList();
      getRootParentCategoryItem(cateId, cateItems, path);
      Collections.reverse(path);
      cateDescBuilder = createSearchedCategoryDescriptionStringBuilder(
          path, cateItem);

      searchedNestedCategoriesMap =
          convertSearchedNestedCategoriesMap(cateDescBuilder.toString(), cateId, cateItems);
      if (MapUtils.isNotEmpty(searchedNestedCategoriesMap)) {
        searchedCategories.putAll(searchedNestedCategoriesMap);
      } else {
        searchedCategories.putIfAbsent(cateId, cateDescBuilder.toString());
      }
    }
    return searchedCategories;
  }

  private static CategoryItem getRootParentCategoryItem(final String id,
      final Map<String, CategoryItem> categoriesByLeafIds, List<String> path) {
    final CategoryItem cateItem = categoriesByLeafIds.get(id);
    final String parentId = cateItem.getParentId();
    path.add(cateItem.getDescription());
    if (StringUtils.isBlank(parentId) || !categoriesByLeafIds.containsKey(parentId)) {
      return cateItem;
    }
    return getRootParentCategoryItem(parentId, categoriesByLeafIds, path);
  }

  private static StringBuilder createSearchedCategoryDescriptionStringBuilder(
      final List<String> path, final CategoryItem currentCategoryItem) {
    final StringBuilder cateDescBuilder = new StringBuilder();

    cateDescBuilder.append(String.join(CATEGORY_NODE_PATH_SEPARATOR, path));
    return cateDescBuilder;
  }

  private static Map<String, String> convertSearchedNestedCategoriesMap(final String parentCateStr,
      final String id, final Map<String, CategoryItem> categoriesByLeafIds) {
    final List<CategoryItem> nestedChildCateItemList =
        getNestedChildrenCategoryItemList(id, categoriesByLeafIds);
    if (CollectionUtils.isEmpty(nestedChildCateItemList)) {
      return Collections.emptyMap();
    }
    final Map<String, String> searchedNestedCategoriesMap = new HashMap<>();
    nestedChildCateItemList.stream()
    .forEach(lastItem -> {
      final String searchedStr = new StringBuilder(parentCateStr).append(StringUtils.SPACE)
          .append(RIGHT_ARROW).append(StringUtils.SPACE)
          .append(lastItem.getDescription()).toString();
      searchedNestedCategoriesMap.putIfAbsent(lastItem.getId(), searchedStr);
    });
    return searchedNestedCategoriesMap;
  }

  private static List<CategoryItem> getNestedChildrenCategoryItemList(final String id,
      final Map<String, CategoryItem> categoriesByLeafIds) {
    return categoriesByLeafIds.values().stream()
        .filter(cateItem -> id.equals(cateItem.getParentId())).collect(Collectors.toList());
  }
}
