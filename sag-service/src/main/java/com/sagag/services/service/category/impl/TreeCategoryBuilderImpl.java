package com.sagag.services.service.category.impl;

import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.service.category.CategoryItemCriteria;
import com.sagag.services.service.category.TreeCategoryBuilder;
import com.sagag.services.service.enums.CategoryType;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class TreeCategoryBuilderImpl
  extends AbstractCategoryBuilder implements TreeCategoryBuilder {

  @Override
  public Map<CategoryType, List<CategoryItem>> buildCategories(
      Collection<CategoryDoc> categoriesOfVehicle, Map<String, CategoryDoc> allCategories,
      CategoryItemCriteria criteria) {
    validateCommonInputs(categoriesOfVehicle, allCategories, criteria);
    final CategoryType[] cateTypes = criteria.getCategoryTypes();
    return super.buildCategories(categoriesOfVehicle, allCategories, criteria, cateTypes);
  }

}
