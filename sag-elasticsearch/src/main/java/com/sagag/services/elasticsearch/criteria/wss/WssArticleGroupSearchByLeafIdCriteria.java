package com.sagag.services.elasticsearch.criteria.wss;

import lombok.Builder;
import lombok.Data;

/**
 * WSS Article Group search by leaf id criteria class.
 */
@Data
@Builder
public class WssArticleGroupSearchByLeafIdCriteria {

  private String leafId;
}
