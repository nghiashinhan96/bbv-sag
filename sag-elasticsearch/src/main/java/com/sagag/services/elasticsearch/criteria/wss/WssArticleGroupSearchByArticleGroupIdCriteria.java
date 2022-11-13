package com.sagag.services.elasticsearch.criteria.wss;

import lombok.Builder;
import lombok.Data;

/**
 * WSS Article Group search by article group id criteria class.
 */
@Data
@Builder
public class WssArticleGroupSearchByArticleGroupIdCriteria {

  private String articleGroupId;
}
