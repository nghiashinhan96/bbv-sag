package com.sagag.services.elasticsearch.criteria.article.wsp;

import com.sagag.services.common.domain.CriteriaDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UniversalPartArticleSearchTerm {

  private List<Integer> genArts;

  private List<Integer> brandIds;

  private List<String> articleGroups;

  private List<String> articleIds;

  private List<CriteriaDto> criterion;

}
