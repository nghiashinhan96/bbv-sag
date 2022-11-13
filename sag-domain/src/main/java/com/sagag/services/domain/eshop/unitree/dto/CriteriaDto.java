package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
// @formatter:off
@JsonPropertyOrder({"cvp", "cid"})
// @formatter:on
public class CriteriaDto implements Serializable {

  private static final long serialVersionUID = 3263340129580651050L;

  private String cvp;
  
  private String cid;
}
