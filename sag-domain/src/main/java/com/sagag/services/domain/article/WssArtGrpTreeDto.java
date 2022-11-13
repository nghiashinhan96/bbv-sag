package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "artgrp", "parentid", "sort", "leafid" })
public class WssArtGrpTreeDto implements Serializable {

  private static final long serialVersionUID = 8888625769857108231L;

  @JsonProperty("artgrp")
  private String artgrp;

  @JsonProperty("parentid")
  private String parentid;

  @JsonProperty("sort")
  private String sort;

  @JsonProperty("leafid")
  private String leafid;
}
