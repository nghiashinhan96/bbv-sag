package com.sagag.services.elasticsearch.dto;

import com.google.common.collect.Maps;
import com.sagag.services.elasticsearch.domain.unitree.UnitreeDoc;
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
public class UnitreeFilteringResponse {


  private Page<UnitreeDoc> unitrees;
  private Map<String, List<SagBucket>> aggregations;

  public static UnitreeFilteringResponse empty() {
    return of(Page.empty());
  }

  public static UnitreeFilteringResponse of(Page<UnitreeDoc> unitrees) {
    return UnitreeFilteringResponse.builder().unitrees(unitrees)
        .aggregations(Maps.newHashMap()).build();
  }

  public boolean hasContent() {
    return this.unitrees != null && this.unitrees.hasContent();
  }

  public boolean hasAggregations() {
    return !MapUtils.isEmpty(this.getAggregations());
  }

}
