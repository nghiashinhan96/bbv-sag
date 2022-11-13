package com.sagag.services.domain.article.oil;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

@Data
public class OilUseInterval implements Serializable {

  private static final long serialVersionUID = -7795833858024131736L;

  @JsonProperty("use_name")
  private String useName;

  @JsonProperty("interval")
  private String interval;

}
