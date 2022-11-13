package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.elasticsearch.domain.category.GtCUPIInfo;
import com.sagag.services.elasticsearch.domain.category.SortInfo;

import lombok.Data;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "gaid", "criteria" })
public class GenArt implements Serializable {

  private static final long serialVersionUID = -3333233207876441770L;

  @Id
  @JsonIgnore
  private long id;

  @JsonProperty("gaid")
  private String gaid;

  @JsonProperty("criteria")
  @Field(type = FieldType.Nested)
  private List<CriteriaProperty> criteria;

  @JsonProperty("brands")
  @Field(type = FieldType.Nested)
  private List<BrandInfo> brands;

  @JsonProperty("cupis")
  @Field(type = FieldType.Nested)
  private List<GtCUPIInfo> cupis;

  @JsonProperty("sort")
  @Field(type = FieldType.Nested)
  private List<SortInfo> sorts;

  @JsonProperty("filters")
  @Field(type = FieldType.Nested)
  private List<FilterBar> filters;
}
