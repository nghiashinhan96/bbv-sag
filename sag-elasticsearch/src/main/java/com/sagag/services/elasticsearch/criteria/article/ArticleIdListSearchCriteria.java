package com.sagag.services.elasticsearch.criteria.article;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleIdListSearchCriteria extends ArticleAggregateCriteria {

  private List<String> articleIdList;

  private boolean useDefaultArtId;

  public List<String> getValidArticleIdList() {
    return this.getArticleIdList().parallelStream()
        .filter(StringUtils::isNotBlank).collect(Collectors.toList());
  }

}
