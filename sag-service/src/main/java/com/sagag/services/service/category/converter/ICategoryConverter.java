package com.sagag.services.service.category.converter;

import com.sagag.services.service.category.CategoryItemCriteria;

public interface ICategoryConverter<T, R> {

  R convert(T categoryDoc, CategoryItemCriteria criteria);
}
