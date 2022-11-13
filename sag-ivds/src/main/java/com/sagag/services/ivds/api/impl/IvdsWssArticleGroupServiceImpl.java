package com.sagag.services.ivds.api.impl;

import com.sagag.services.domain.article.WssArticleGroupDocDto;
import com.sagag.services.domain.article.WssArticleGroupDto;
import com.sagag.services.elasticsearch.api.WssArticleGroupSearchService;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticleGroupSearchSortCriteria;
import com.sagag.services.elasticsearch.criteria.wss.WssArticlegroupSearchTermCriteria;
import com.sagag.services.elasticsearch.dto.WssArticleGroupSearchResponse;
import com.sagag.services.ivds.api.IvdsWssArticleGroupService;
import com.sagag.services.ivds.converter.wss.WssArticleGroupConverters;
import com.sagag.services.ivds.request.WssArticleGroupSearchRequest;

import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation class for IVDS WSS article group service.
 */
@Service
public class IvdsWssArticleGroupServiceImpl implements IvdsWssArticleGroupService {

  @Autowired
  private WssArticleGroupSearchService wssArticleGroupSearchService;

  @Override
  public Page<WssArticleGroupDto> searchWssArticleGroup(WssArticleGroupSearchRequest request) {
    final Pageable pageable = request.getPageRequest();
    final WssArticleGroupSearchCriteria criteria = initWssArticleGroupSearchCriteria(request);
    final WssArticleGroupSearchResponse wssArticleGroupSearchResponse =
        wssArticleGroupSearchService.searchWssArticleGroupByCriteria(criteria, pageable);
    return wssArticleGroupSearchResponse.getArticleGroups()
        .map(WssArticleGroupConverters.wssArticleGroupDtoConverter());
  }

  @Override
  public Optional<WssArticleGroupDocDto> findExactByArticleGroupLeafId(String leafId) {
    return wssArticleGroupSearchService.findByLeafId(leafId)
        .map(WssArticleGroupConverters.simpleWssArticleGroupConverter());
  }

  @Override
  public Optional<WssArticleGroupDocDto> findExactByArticleGroupId(String articleGroupId) {
    return wssArticleGroupSearchService.findByArticleGroupId(articleGroupId)
        .map(WssArticleGroupConverters.simpleWssArticleGroupConverter());
  }

  private static WssArticleGroupSearchCriteria initWssArticleGroupSearchCriteria(
      WssArticleGroupSearchRequest request) {
    WssArticlegroupSearchTermCriteria criteriaSearchTerm = initWssArticlegroupSearchTerm(request);
    WssArticleGroupSearchSortCriteria wssArticleGroupSearchSort =
        initWssArticleGroupSearchSortCriteria();
    return WssArticleGroupSearchCriteria.builder()
        .searchTerm(criteriaSearchTerm).sort(wssArticleGroupSearchSort).build();
  }

  private static WssArticleGroupSearchSortCriteria initWssArticleGroupSearchSortCriteria() {
    return WssArticleGroupSearchSortCriteria.builder().articleGroup(SortOrder.ASC).build();
  }

  private static WssArticlegroupSearchTermCriteria initWssArticlegroupSearchTerm(
      WssArticleGroupSearchRequest request) {
    return WssArticlegroupSearchTermCriteria.builder().articleGroup(request.getArticleGroup())
        .articleGroupDesc(request.getArticleGroupDesc()).build();
  }
}
