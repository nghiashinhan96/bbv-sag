package com.sagag.services.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "cid", "cvp" })
public class CriteriaDto implements Serializable {

  private static final long serialVersionUID = -7471604086491648341L;

  @JsonIgnore
  private long id;

  @JsonProperty("cid")
  private String cid;

  @JsonProperty("cvp")
  private String cvp;

}
