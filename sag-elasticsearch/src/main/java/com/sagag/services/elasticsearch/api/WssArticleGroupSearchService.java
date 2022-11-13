package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchCriteria;
import com.sagag.services.elasticsearch.domain.wss.WssArticleGroupDoc;
import com.sagag.services.elasticsearch.dto.WssArticleGroupSearchResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

/**
 * Elasticsearch service for WSS article group.
 */
public interface WssArticleGroupSearchService extends IFetchAllDataService<WssArticleGroupDoc> {

  /**
   * Returns top 10 available article groups.
   * <p>
   * only used for IT tests
   *
   * @return a list of {@link WssArticleGroupDoc}.
   */
  public List<WssArticleGroupDoc> getTop10ArticleGroup();

  WssArticleGroupSearchResponse searchWssArticleGroupByCriteria(
      WssArticleGroupSearchCriteria criteria, Pageable pageable);

  Optional<WssArticleGroupDoc> findByLeafId(String leafId);

  Optional<WssArticleGroupDoc> findByArticleGroupId(String articleGroupId);
}
