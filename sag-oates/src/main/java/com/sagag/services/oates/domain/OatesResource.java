package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.Data;

@Data
public class OatesResource implements Serializable {

  private static final long serialVersionUID = 7768711047854212636L;

  @JsonProperty("@href")
  private Map<String, String> href;

  @JsonProperty("@type")
  private String type;

  @JsonCreator
  public OatesResource(@JsonProperty("@href") String href, @JsonProperty("@type") String type) {
    this.href = SagJSONUtil.jsonToMap(href);
    this.type = type;
  }

}
