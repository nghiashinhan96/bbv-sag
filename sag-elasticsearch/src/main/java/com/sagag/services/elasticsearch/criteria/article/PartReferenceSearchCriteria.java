package com.sagag.services.elasticsearch.criteria.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PartReferenceSearchCriteria extends ArticleAggregateCriteria {

  private List<String> partNrs;

  private boolean usePartsExt;
}
