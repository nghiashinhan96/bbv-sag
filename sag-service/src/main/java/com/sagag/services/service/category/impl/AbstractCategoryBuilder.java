package com.sagag.services.service.category.impl;

import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.service.category.CategoryItemCriteria;
import com.sagag.services.service.category.CategoryTreeCombinator;
import com.sagag.services.service.enums.CategoryType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractCategoryBuilder {

  @Autowired
  protected CategoryTreeCombinator categoryTreeCombinator;

  protected static void validateCommonInputs(final Collection<CategoryDoc> categoriesOfVehicle,
      final Map<String, CategoryDoc> allCategories, final CategoryItemCriteria criteria) {
    Assert.notEmpty(categoriesOfVehicle, "The given categories of selected vehicle must not be empty");
    Assert.notEmpty(allCategories, "The given all categories must not be empty");
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notNull(criteria.getAff(), "The given affiliate must not be null");
  }

  /**
   * Builds categories by category type.
   *
   * @param categoriesOfVehicle
   * @param allCategories
   * @param criteria
   * @param categoryTypes
   * @return the map of category tree nodes of category type.
   */
  protected Map<CategoryType, List<CategoryItem>> buildCategories(
      final Collection<CategoryDoc> categoriesOfVehicle,
      final Map<String, CategoryDoc> allCategories,
      final CategoryItemCriteria criteria,
      final CategoryType... categoryTypes) {
    if (ArrayUtils.isEmpty(categoryTypes)
        || CollectionUtils.isEmpty(categoriesOfVehicle)
        || MapUtils.isEmpty(allCategories)) {
      return Collections.emptyMap();
    }

    final Map<CategoryType, List<CategoryItem>> categoriesMap = new EnumMap<>(CategoryType.class);
    Stream.of(categoryTypes)
    .forEach(cateType -> {
      criteria.setCategoryType(cateType);
      final Map<String, CategoryItem> categories = categoryTreeCombinator.combine(
          categoriesOfVehicle, allCategories, criteria);
      categoriesMap.put(cateType, buildCateTreeList(categories));
    });
    return categoriesMap;
  }

  private static List<CategoryItem> buildCateTreeList(
      final Map<String, CategoryItem> categoriesByLeafIds) {

    // Get list leafId from elastic search by categoryText
    final Map<String, CategoryItem> categoriesByLeafIdsFiltered =
        new ConcurrentHashMap<>(categoriesByLeafIds);

    final List<CategoryItem> tempCats = new ArrayList<>(categoriesByLeafIdsFiltered.values());

 // Sort categories with custom comparator
    Collections.sort(tempCats, sortCategoryComparator());

    tempCats.stream().forEach(catItem -> {
      final String parentId = catItem.getParentId();
      final CategoryItem child = categoriesByLeafIdsFiltered.get(catItem.getId());
      if (!StringUtils.isBlank(parentId) && categoriesByLeafIdsFiltered.containsKey(parentId)) {
        // category has parent
        final CategoryItem parent = categoriesByLeafIdsFiltered.get(parentId);
        parent.addChildItem(child);
        child.setParentId(parentId);
      } else {
        child.setParentId(StringUtils.EMPTY);
      }
    });

    // Sort children categories with custom comparator by category id
    tempCats.stream().forEach(tempCate -> sortLastChildCategoriesByCateId(tempCate.getChildren()));

    final List<CategoryItem> parents = categoriesByLeafIdsFiltered.values().stream()
        .filter(catItem -> StringUtils.isBlank(catItem.getParentId())).collect(Collectors.toList());
    Collections.sort(parents, sortCategoryComparator());
    // update children root category description

    parents.parallelStream().forEach(rootCat -> {
      // the root parent is itself a root category
      rootCat.setRootDescription(rootCat.getDescription());
      if (rootCat.hasChildren()) {
        updateChildrenRootCategory(rootCat, rootCat.getChildren());
      }
    });

    return parents;
  }

  private static void sortLastChildCategoriesByCateId(List<CategoryItem> categories) {
    if (CollectionUtils.isEmpty(categories)) {
      return;
    }
    Collections.sort(categories, categoryComparator());
  }

  private static Comparator<CategoryItem> categoryComparator() {
    return (cate1, cate2) -> {
      if (StringUtils.isBlank(cate1.getId()) || StringUtils.isBlank(cate2.getId())) {
        return -1;
      }
      return NumberUtils.createInteger(cate1.getId())
          .compareTo(NumberUtils.createInteger(cate2.getId()));
    };
  }

  private static Comparator<CategoryItem> sortCategoryComparator() {
    return (cate1, cate2) -> {
      if (StringUtils.isBlank(cate1.getSort())) {
        return -1; // let the category item which has empty sort to the end.
      }
      return cate1.getSort().compareTo(cate2.getSort());
    };
  }

  private static void updateChildrenRootCategory(final CategoryItem rootCat,
      final List<CategoryItem> children) {
    children.parallelStream().forEach(child -> {
      child.setRootDescription(rootCat.getDescription());
      if (child.hasChildren()) {
        updateChildrenRootCategory(rootCat, child.getChildren());
      }
    });
  }

}
