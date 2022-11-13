package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.AbstractElasticsearchService;
import com.sagag.services.elasticsearch.api.LicensePlateSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.vehicle.licenseplate.LicensePlateDoc;
import com.sagag.services.elasticsearch.query.vehicles.LicensePlateQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class LicensePlateSearchServiceImpl extends AbstractElasticsearchService
  implements LicensePlateSearchService {

  private static final Pageable DF_PAGEABLE = PageUtils.DEF_PAGE;

  @Autowired
  private LicensePlateQueryBuilder licensePlateQueryBuilder;

  @Override
  public String keyAlias() {
    return "license_plate";
  }

  @Override
  public List<LicensePlateDoc> searchLicensePlateByText(final String text) {
    log.debug("Returning the all data documents by text = {}", text);
    if (StringUtils.isBlank(text)) {
      return Collections.emptyList();
    }
    final KeywordArticleSearchCriteria criteria =
      new KeywordArticleSearchCriteria(text, ArrayUtils.EMPTY_STRING_ARRAY);
    final SearchQuery searchQuery =
        licensePlateQueryBuilder.buildQuery(criteria, DF_PAGEABLE, index());

    return searchList(searchQuery, LicensePlateDoc.class);
  }
}
