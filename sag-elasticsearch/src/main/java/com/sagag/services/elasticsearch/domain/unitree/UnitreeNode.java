package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.dozer.Mapping;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Data
//@formatter:off
@JsonPropertyOrder({
  "leaf_id",
  "parent_id",
  "open",
  "brand_prio",
  "filter_default",
  "filter_bar",
  "include",
  "node_external_service_attribute",
  "node_name",
  "filter_caid",
  "node_external_type",
  "exclude",
  "filter_sort",
  "sort"
})
//@formatter:on
public class UnitreeNode implements Serializable {

  private static final long serialVersionUID = -4205770288471800847L;

  @JsonProperty("open")
  private String open;

  @JsonProperty("brand_prio")
  private Integer brandPrio;

  @JsonProperty("filter_default")
  private String filterDefault;

  @JsonProperty("filter_bar")
  private String filterBar;

  @Mapping("include")
  @JsonProperty("include")
  private Include include;

  @JsonProperty("node_external_service_attribute")
  private String nodeExternalServiceAttribute;

  @JsonProperty("node_name")
  private String nodeName;

  @Mapping("id")
  @JsonProperty("leaf_id")
  private String leafId;

  @JsonProperty("parent_id")
  private String parentId;

  @JsonProperty("filter_caid")
  private String filterCaid;

  @JsonProperty("node_external_type")
  private String nodeExternalType;

  @Mapping("exclude")
  @JsonProperty("exclude")
  private Exclude exclude;

  @JsonProperty("filter_sort")
  private String filterSort;

  @JsonProperty("sort")
  private String sort;

  @JsonProperty("node_keywords")
  private String nodeKeywords;
  
  @JsonProperty("filters")
  @Field(type = FieldType.Nested)
  private List<Filter> filters;

  public boolean isRootNode() {
    return StringUtils.equalsIgnoreCase(parentId, leafId);
  }
}
