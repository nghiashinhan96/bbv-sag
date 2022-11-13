package com.sagag.services.elasticsearch.domain.unitree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
//@formatter:off
@JsonPropertyOrder({
  "cvp",
  "cid",
})
//@formatter:on
public class Criteria implements Serializable {

  private static final long serialVersionUID = -4044231556210746439L;

  @JsonProperty("cvp")
  private String cvp;

  @JsonProperty("cid")
  private String cid;


}
