package com.sagag.services.article.api.builder;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.contants.SagConstants;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public interface AdditionalCriteriaBuilder {

  /**
   * Builds the optional of additional search criteria.
   *
   */
  Optional<AdditionalSearchCriteria> build(Optional<String> searchString,
      Optional<String> kTypeNr, boolean isDetailRequest, Integer finalCustomerId);

  default Optional<AdditionalSearchCriteria> buildVehicleAdditional(
      Optional<String> kTypeNr) {
    return build(Optional.empty(), kTypeNr, false, null);
  }

  default Optional<AdditionalSearchCriteria> buildVehicleAdditional(Optional<String> kTypeNr,
      boolean isUsingV2) {
    Optional<AdditionalSearchCriteria> build = build(Optional.empty(), kTypeNr, false, null);
    if (build.isPresent()) {
      build.get().setIsExcludeSubArticles(isUsingV2);
    } else {
      build = Optional
          .of(AdditionalSearchCriteria.builder().isExcludeSubArticles(isUsingV2).build());
    }
    return build;
  }

  default Optional<AdditionalSearchCriteria> buildVehicleAdditional(String searchStr,
      Optional<String> kTypeNr) {
    return build(Optional.ofNullable(searchStr), kTypeNr, false, null);
  }

  default Optional<AdditionalSearchCriteria> buildNoVehicleAdditional(
      String searchString) {
    return build(Optional.ofNullable(searchString), Optional.empty(), false,
        null);
  }

  default Optional<AdditionalSearchCriteria> buildDetailAdditional(String articleId) {
    return build(Optional.ofNullable(articleId), Optional.empty(), true, null);
  }

  default Optional<AdditionalSearchCriteria> buildDetailAdditional(List<String> articleIds) {
    return build(Optional.ofNullable(StringUtils.join(articleIds, SagConstants.COMMA_NO_SPACE)),
        Optional.empty(), true, null);
  }

  default Optional<AdditionalSearchCriteria> buildDetailAdditional(String articleId,
      Optional<String> kTypeNr) {
    return build(Optional.ofNullable(articleId), kTypeNr, true, null);
  }

  default Optional<AdditionalSearchCriteria> buildDetailAdditional(List<String> articleIds,
      Optional<String> kTypeNr) {
    return build(Optional.ofNullable(StringUtils.join(articleIds, SagConstants.COMMA_NO_SPACE)),
        kTypeNr, true, null);
  }

  default Optional<AdditionalSearchCriteria> buildNoVehicleAdditional(String searchString,
      Integer finalCustomerId, Boolean isIncludeErpSync) {
    Optional<AdditionalSearchCriteria> build =
        build(Optional.ofNullable(searchString), Optional.empty(), false, finalCustomerId);
    if (build.isPresent()) {
      build.get().setIsExcludeSubArticles(isIncludeErpSync);
    } else {
      build = Optional
          .of(AdditionalSearchCriteria.builder().isExcludeSubArticles(isIncludeErpSync).build());
    }
    return build;
  }

  default Optional<AdditionalSearchCriteria> buildNoVehicleAdditional(boolean isIncludeErpSync) {
      return Optional
          .of(AdditionalSearchCriteria.builder().isExcludeSubArticles(isIncludeErpSync).build());
  }
}
