package com.sagag.services.service.category;

import java.util.Collection;
import java.util.Map;

@FunctionalInterface
public interface ICategoryBuilder<T, R> {

  /**
   * Builds categories functional method.
   *
   */
  R buildCategories(Collection<T> categoriesOfVehicle, Map<String, T> allCategories,
      CategoryItemCriteria criteria);
}
