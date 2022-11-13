package com.sagag.services.domain.eshop.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder({
      "brand",
      "prio",
      "subPrio"
      })
//@formatter:on
public class BrandDto implements Serializable {

  private static final long serialVersionUID = 6237165636418934176L;

  private static final int KEY_PRIOR = 1;

  private static final int KEY_SUB_PRIOR = 2;

  @JsonProperty("brand")
  private String brand;

  private String gaId;

  private String affiliate;

  private String collection;

  @JsonProperty("sort_map")
  private Map<Integer, Integer> sorts;

  @JsonProperty("prio")
  public Integer getFirstPriority() {
    return MapUtils.getInteger(sorts, KEY_PRIOR, null);
  }

  @JsonProperty("subPrio")
  public Integer getSubPriority() {
    return MapUtils.getInteger(sorts, KEY_SUB_PRIOR, null);
  }
}
