package com.sagag.services.ivds.request;

import com.sagag.services.domain.article.ArticlePartItemDto;
import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.ivds.request.filter.ArticleSearchCriteriaConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Request body class for WSP search.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePartListSearchRequest
    implements Serializable, ArticleSearchCriteriaConverter<ArticleIdListSearchCriteria> {

  private static final long serialVersionUID = -6046715137948099020L;

  private List<ArticlePartItemDto> partListItems;

  @Override
  public ArticleIdListSearchCriteria toCriteria() {
    List<String> partListArticleIds = CollectionUtils.emptyIfNull(partListItems).stream()
        .map(ArticlePartItemDto::getPartsListItemIdArt)
        .filter(partsListItemIdArt -> !Objects.isNull(partsListItemIdArt)).map(Object::toString)
        .collect(Collectors.toList());
    ArticleIdListSearchCriteria criteria = new ArticleIdListSearchCriteria();
    criteria.setArticleIdList(partListArticleIds);
    return criteria;
  }

}
