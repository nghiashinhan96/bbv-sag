package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OatesResourceDto implements Serializable {

  private static final long serialVersionUID = 7768711047854212636L;

  private Map<String, String> href;

  private String type;

  @JsonCreator
  public OatesResourceDto(@JsonProperty("@href") String href,
      @JsonProperty("@type") String type) {
    this.href = SagJSONUtil.jsonToMap(href);
    this.type = type;
  }

}
