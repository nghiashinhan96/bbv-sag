package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.CriteriaDoc;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CriteriaSearchService extends IFetchAllDataService<CriteriaDoc> {

  /**
   * Returns all available criteria.
   *
   * @return a list of {@link CriteriaDoc}
   */
  Page<CriteriaDoc> getAllCriteria(Pageable pageable);

  /**
   * Returns top 10 criteria.
   * <p>
   * only used for IT tests to reduce performance.
   *
   * @return a list of {@link CriteriaDoc}
   */
  List<CriteriaDoc> getTop10Criteria();

}
