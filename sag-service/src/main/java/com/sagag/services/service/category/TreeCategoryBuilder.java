package com.sagag.services.service.category;

import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.service.enums.CategoryType;

import java.util.List;
import java.util.Map;

public interface TreeCategoryBuilder
    extends ICategoryBuilder<CategoryDoc, Map<CategoryType, List<CategoryItem>>> {

}
