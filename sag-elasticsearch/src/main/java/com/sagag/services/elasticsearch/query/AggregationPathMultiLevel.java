package com.sagag.services.elasticsearch.query;


import com.sagag.services.elasticsearch.enums.IAttributePath;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AggregationPathMultiLevel {
  private IAttributePath path;
  private List<AggregationPathMultiLevel> subPaths;

  public AggregationPathMultiLevel(IAttributePath path) {
    this.path = path;
  }

  public AggregationPathMultiLevel(IAttributePath path, IAttributePath subPath) {
    this.path = path;
    this.subPaths = Arrays.asList(new AggregationPathMultiLevel(subPath));
  }
}
