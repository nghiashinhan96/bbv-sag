package com.sagag.services.elasticsearch.criteria.article;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReferenceAndArtNumSearchCriteria extends ArticleAggregateCriteria {

  private List<String> articleNrs;

  private Map<String, Set<String>> referenceByGenArtIdMap;

  private boolean useGenArtIdInQuery;

  private boolean fromFreetextSearch;

  private boolean disableIgnoreGenArtMatch;

  public void disableGenArtIdInQuery() {
    this.useGenArtIdInQuery = false;
  }
}
