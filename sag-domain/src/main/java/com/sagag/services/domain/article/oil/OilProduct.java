package com.sagag.services.domain.article.oil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@JsonPropertyOrder({ "name", "code", "id", "component", "intervals", "appNotes" })
public class OilProduct implements Serializable {

  private static final long serialVersionUID = 655985649019529682L;

  @JsonProperty("name")
  private String name;

  @JsonProperty("code")
  private String code;

  @JsonProperty("id")
  private String id;

  @JsonProperty("capacities")
  private List<String> capacities;

  @JsonProperty("intervals")
  private List<OilUseInterval> intervals;

  @JsonProperty("appNotes")
  private List<String> appNotes;

  @JsonIgnore
  private Set<String> pimIds;

  @JsonIgnore
  public boolean containPimId(String pimId) {
    if (StringUtils.isBlank(pimId)) {
      return false;
    }
    return getPimIds().contains(pimId);
  }
}
