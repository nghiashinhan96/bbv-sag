package com.sagag.services.autonet.erp.builder;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.profiles.AutonetProfile;

@Component
@AutonetProfile
public class AutonetAdditionalCriteriaBuilder implements AdditionalCriteriaBuilder {

  @Override
  public Optional<AdditionalSearchCriteria> build(Optional<String> searchString,
      Optional<String> kTypeNr, boolean isDetailRequest, Integer finalCustomerId) {
    AdditionalSearchCriteria criteria = AdditionalSearchCriteria.builder()
        .detailArticleRequest(isDetailRequest)
        .searchString(searchString.orElseGet(() -> StringUtils.EMPTY))
        .kTypeNr(kTypeNr.orElseGet(() -> StringUtils.EMPTY))
        .build();
    return Optional.of(criteria);
  }

}
