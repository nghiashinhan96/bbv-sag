package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.CategoryDoc;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Elasticsearch service for Category document.
 */
public interface CategoryService extends IFetchAllDataService<CategoryDoc> {

  /**
   * Returns a list of all categories {@link CategoryDoc}.
   *
   * @return a list of {@link CategoryDoc}.
   */
  Page<CategoryDoc> getAllCategories(Pageable pageable);

  /**
   * Return a list of categories based of search text.
   *
   * @param leaftxt the leaftxt search text
   * @return a list of {@link CategoryDoc}} based on search text (handle misspelling, synonym,
   *         partial search).
   */
  List<CategoryDoc> getCategoriesByLeaftxt(String leaftxt);

}
