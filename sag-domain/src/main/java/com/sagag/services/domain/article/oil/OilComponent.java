package com.sagag.services.domain.article.oil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Data
@JsonPropertyOrder({ "name", "code", "group_id", "capacities", "intervals" })
public class OilComponent implements Serializable {

  private static final long serialVersionUID = -8296088799405544678L;

  @JsonProperty("name")
  private String name;

  @JsonProperty("code")
  private String code;

  @JsonProperty("groupid")
  private String groupid;

  @JsonProperty("capacities")
  private Collection<String> capacities;

  @JsonProperty("intervals")
  private Collection<OilUseInterval> intervals;

}
