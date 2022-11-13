package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.elasticsearch.api.impl.articles.wsp.UniversalPartArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.dto.UniversalPartArticleResponse;
import com.sagag.services.ivds.api.IvdsArticleInWSPContextService;
import com.sagag.services.ivds.request.WSPSearchRequest;
import com.sagag.services.ivds.response.wsp.UniversalPartArticleSearchResponse;
import com.sagag.services.ivds.utils.BrandAndGenArtCombinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IvdsArticleInWSPContextServiceImpl extends ArticleProcessor
    implements IvdsArticleInWSPContextService {

  @Autowired
  private UniversalPartArticleSearchServiceImpl universalPartSearchService;

  @Autowired
  private BrandAndGenArtCombinator brandAndGenArtCombinator;

  @Override
  public UniversalPartArticleSearchResponse searchUniversalPartArticlesByRequest(
      final UserInfo user, final WSPSearchRequest request, final Pageable pageable) {
    UniversalPartArticleSearchCriteria criteria = request.toCriteria();

    criteria.setAffNameLocks(findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    UniversalPartArticleResponse articles = universalPartSearchService.search(criteria, pageable);

    return brandAndGenArtCombinator.buildUniversalPartArticleSearchResponse(articles,
        request.getLanguage(), user.getSupportedAffiliate());
  }

}
