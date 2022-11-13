package com.sagag.services.ivds.converter.wsp;

import com.sagag.services.common.domain.CriteriaDto;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchTerm;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchTerm.UniversalPartArticleSearchTermBuilder;
import com.sagag.services.ivds.domain.WSPCriteriaDto;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Function;

/**
 * Utility provide some converters of WSP.
 */
@UtilityClass
public final class UniversalPartConverters {

  public static Function<WSPCriteriaDto, UniversalPartArticleSearchTerm> simpleWspCriteriaArticleSearchConverter() {
    return criteria -> {
      final UniversalPartArticleSearchTermBuilder builder =
          UniversalPartArticleSearchTerm.builder();
      builder.genArts(criteria.getGenArts()).brandIds(criteria.getBrandIds())
          .articleGroups(criteria.getArticleGroups()).articleIds(criteria.getArticleIds());
      List<CriteriaDto> criterion = criteria.getCriterion();
      if (CollectionUtils.isNotEmpty(criterion)) {
        builder.criterion(criterion);
      }
      return builder.build();
    };
  }


}
