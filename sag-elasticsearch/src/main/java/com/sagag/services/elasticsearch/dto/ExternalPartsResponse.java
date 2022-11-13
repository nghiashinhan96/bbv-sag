package com.sagag.services.elasticsearch.dto;

import com.google.common.collect.Maps;
import com.sagag.services.elasticsearch.domain.article.ExternalPartDoc;
import com.sagag.services.elasticsearch.extractor.SagBucket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalPartsResponse {

  private Page<ExternalPartDoc> externalParts;
  private Map<String, List<SagBucket>> aggregations;

  public static ExternalPartsResponse empty() {
    return of(Page.empty());
  }

  public static ExternalPartsResponse of(Page<ExternalPartDoc> externalParts) {
    return ExternalPartsResponse.builder().externalParts(externalParts).aggregations(Maps.newHashMap()).build();
  }

  /**
   * Checks if the response has content or not.
   *
   * @return <code> true </code> if the response has external parts, <code>false</code> otherwise.
   */
  public boolean hasContent() {
    return this.externalParts != null && this.externalParts.hasContent();
  }

  public boolean hasAggregations() {
    return !MapUtils.isEmpty(this.getAggregations());
  }
}
