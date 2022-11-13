package com.sagag.services.elasticsearch.criteria.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAggregateMultiLevel implements Serializable {

  private static final long serialVersionUID = -8053231324803377360L;

  private String id;
  private List<ArticleAggregateMultiLevel> subIds;
}
