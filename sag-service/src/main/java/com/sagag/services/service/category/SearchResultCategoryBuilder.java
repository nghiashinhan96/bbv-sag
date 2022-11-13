package com.sagag.services.service.category;

import com.sagag.services.elasticsearch.domain.CategoryDoc;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Map;

public interface SearchResultCategoryBuilder
  extends ICategoryBuilder<CategoryDoc, Map<String, String>> {

  Map<String, String> buildCategories(Collection<CategoryDoc> categoriesOfVehicle,
      Map<String, CategoryDoc> allCategories, Page<CategoryDoc> searchedCategoriesPage,
      CategoryItemCriteria criteria);
}
