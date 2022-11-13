package com.sagag.services.service.category;

import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;

import java.util.List;

public interface QuickClickCategoryBuilder
    extends ICategoryBuilder<CategoryDoc, List<List<CategoryItem>>> {

}
