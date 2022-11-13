package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
//@formatter:off
@JsonPropertyOrder({
  "id_dlnr",
  "genarts",
  "artgrp",
  "artids",
  "brands",
  "criteria"
})
//@formatter:on
public class Include implements Serializable {

  private static final long serialVersionUID = 910189764283510025L;

  @JsonProperty("genarts")
  private String genarts;

  @JsonProperty("artids")
  private String artids;

  @JsonProperty("brands")
  private String brands;

  @JsonProperty("id_dlnr")
  private String idDlnr;

  @JsonProperty("artgrp")
  private String artgrp;
  
  @JsonProperty("criteria")
  private List<Criteria> criteria;

}
