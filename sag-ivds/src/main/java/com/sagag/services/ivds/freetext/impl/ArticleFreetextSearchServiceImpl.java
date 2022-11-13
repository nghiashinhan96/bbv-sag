package com.sagag.services.ivds.freetext.impl;

import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.freetext.IFreetextSearchService;
import com.sagag.services.ivds.freetext.SearchOptions;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.response.FreetextResponseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleFreetextSearchServiceImpl implements IFreetextSearchService {

  @Autowired
  private IvdsArticleService articleService;

  @Override
  public void search(FreetextSearchRequest request, FreetextResponseDto response) {
    // Lower-case the search text
    FreetextSearchRequest clonedRequest = FreetextSearchRequest.builder()
        .text(request.getText())
        .user(request.getUser())
        .fitering(request.getFitering())
        .isFullRequest(request.isFullRequest())
        .searchOptions(request.getSearchOptions())
        .pageRequest(request.getPageRequest())
        .build();

    ArticleFilteringResponseDto freetextArtResponse = articleService.searchFreetext(clonedRequest);
    response.setArticles(freetextArtResponse.getArticles());
    response.setContextKey(freetextArtResponse.getContextKey());
  }

  @Override
  public boolean support(List<String> options) {
    return options.contains(SearchOptions.ARTICLES.lowerCase());
  }

}
