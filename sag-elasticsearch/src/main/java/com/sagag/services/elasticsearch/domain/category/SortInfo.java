package com.sagag.services.elasticsearch.domain.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
// @formatter:off
@JsonPropertyOrder({
  "brand",
  "prio"
})
//@formatter:on
public class SortInfo implements Serializable {

  private static final long serialVersionUID = -3947986987434483823L;

  @JsonProperty("aid")
  private String affiliateId;

  @JsonProperty("prio")
  private String priority;
}
