package com.sagag.services.elasticsearch.extractor;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SagBucket {
  private Object key;
  private long docCount;
  private List<Map<String, List<SagBucket>>> subBuckets;

}
