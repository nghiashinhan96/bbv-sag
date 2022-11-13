package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.criteria.ExternalPartsSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import com.sagag.services.elasticsearch.dto.ExternalPartsResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ExternalPartsSearchService {

  /**
   * Returns the external part by criteria.
   *
   * @param criteria
   * @return the result of {@link ExternalPartsResponse}
   */
  ExternalPartsResponse searchByCriteria(ExternalPartsSearchCriteria criteria);

  /**
   * Returns the external part by artIds.
   *
   * @param artIds
   * @param pageable
   * @return the result of {@link Page}
   */
  List<ExternalPartDoc> searchByArtIds(List<String> artIds);
}
