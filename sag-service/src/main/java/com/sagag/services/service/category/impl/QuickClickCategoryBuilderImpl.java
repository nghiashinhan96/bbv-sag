package com.sagag.services.service.category.impl;

import com.google.common.collect.Lists;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.service.category.CategoryItemCriteria;
import com.sagag.services.service.category.QuickClickCategoryBuilder;
import com.sagag.services.service.enums.CategoryType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class QuickClickCategoryBuilderImpl extends AbstractCategoryBuilder
    implements QuickClickCategoryBuilder {

  private static final Integer Q_FLAG_ON = 1;

  private static final String COLUMN_PREFIX = "COLUMN_";

  @Override
  public List<List<CategoryItem>> buildCategories(Collection<CategoryDoc> categoriesOfVehicle,
      Map<String, CategoryDoc> allCategories, CategoryItemCriteria criteria) {
    validateCommonInputs(categoriesOfVehicle, allCategories, criteria);

    final CategoryType categoryType = criteria.getCategoryType();
    final Map<CategoryType, List<CategoryItem>> categoriesMap =
        super.buildCategories(categoriesOfVehicle, allCategories, criteria, categoryType);
    final List<CategoryItem> availableAllCategories = categoriesMap.get(categoryType);
    if (CollectionUtils.isEmpty(availableAllCategories)) {
      return Collections.emptyList();
    }
    List<CategoryItem> categories = new ArrayList<>();

    availableAllCategories.forEach(cate -> {
      if (isCategoryHasOnlyTwoLevels(cate)) {
        thenQuickclickWillIncludeTheRootParent(categories, cate);
      } else {
        categories.addAll(ListUtils.emptyIfNull(cate.getChildren()).stream()
            .filter(isDisplayedOnQuickClickTreePredicate()).collect(Collectors.toList()));
      }
    });

    // Initialize empty tree map
    final Map<String, List<CategoryItem>> qColCatMap = new TreeMap<>();
    categories.stream().forEach(cateItem -> {
      final String curKey = COLUMN_PREFIX + cateItem.getQCol();
      qColCatMap.compute(curKey, categoriesRemapping(curKey, cateItem));
    });

    // Sort children list by qrow and qsort
    qColCatMap.forEach((key, items) -> {
      if (CollectionUtils.isEmpty(items)) {
        return;
      }
      Collections.sort(items, sortCategoryByQRow());
      items.forEach(cat -> Collections.sort(cat.getChildren(), sortCategoryByQSort()));
    });
    return Lists.newArrayList(qColCatMap.values());
  }

  private void thenQuickclickWillIncludeTheRootParent(List<CategoryItem> categories,
      CategoryItem cate) {
    if (Optional.ofNullable(cate).filter(isDisplayedOnQuickClickTreePredicate()).isPresent()) {
      categories.add(cate);
    }
  }

  private boolean isCategoryHasOnlyTwoLevels(CategoryItem cate) {
    if (cate == null) {
      return false;
    }

    List<CategoryItem> secondLevelCategory = cate.getChildren();
    List<CategoryItem> childs = CollectionUtils.emptyIfNull(secondLevelCategory).stream()
        .filter(hasChildCategory()).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(childs)) {
      return true;
    }

    return false;
  }

  private Predicate<CategoryItem> hasChildCategory() {
    return cate -> CollectionUtils.isNotEmpty(cate.getChildren());
  }

  private static Predicate<CategoryItem> isDisplayedOnQuickClickTreePredicate() {
    return catTree -> Q_FLAG_ON.equals(catTree.getQFlag())
      && isContainShowedChildrenNodePredicate().test(catTree);
  }

  private static Predicate<CategoryItem> isContainShowedChildrenNodePredicate() {
    return categoryItem -> ListUtils.emptyIfNull(categoryItem.getChildren()).stream()
      .anyMatch(CategoryItem::isQShow);
  }

  private static BiFunction<String, List<CategoryItem>, List<CategoryItem>> categoriesRemapping(
      String curKey, CategoryItem cateItem) {
    return (key, values) -> {
      if (values == null) {
        values = Lists.newArrayList();
      }
      if (StringUtils.equals(key, curKey)) {
        values.add(cateItem);
      }
      return values;
    };
  }

  private static Comparator<CategoryItem> sortCategoryByQSort() {
    return (cat1, cat2) -> sortCategoryItemByField(cat1.getDefaultQSort(), cat2.getDefaultQSort());
  }

  private static Comparator<CategoryItem> sortCategoryByQRow() {
    return (cat1, cat2) -> sortCategoryItemByField(cat1.getDefaultQRow(), cat2.getDefaultQRow());
  }

  private static int sortCategoryItemByField(int catField1, int catField2) {
    return NumberUtils.compare(catField1, catField2);
  }
}
