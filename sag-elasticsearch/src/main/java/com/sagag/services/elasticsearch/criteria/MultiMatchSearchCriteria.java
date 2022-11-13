package com.sagag.services.elasticsearch.criteria;

import com.sagag.services.elasticsearch.enums.IndexFieldType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;

@Data
@Builder
public class MultiMatchSearchCriteria {

  private String text;
  private String fields;
  private Type typeSearch;
  private IndexFieldType fieldType;
  private String path;

  @Tolerate
  public MultiMatchSearchCriteria() {
    // provide No Argument Constructor
  }

}
