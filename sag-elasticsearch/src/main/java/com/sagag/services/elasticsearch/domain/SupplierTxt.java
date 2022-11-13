package com.sagag.services.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "dlnrid", "suppname" })
public class SupplierTxt implements Serializable {

  private static final long serialVersionUID = 7025128330229710602L;

  @JsonProperty("dlnrid")
  private String dlnrid;

  @JsonProperty("suppname")
  private String suppname;
}
