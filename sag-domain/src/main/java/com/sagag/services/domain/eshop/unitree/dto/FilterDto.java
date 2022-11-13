package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
// @formatter:off
@JsonPropertyOrder({"gaid", "filter"})
// @formatter:on
public class FilterDto implements Serializable {

  private static final long serialVersionUID = -3423006970887877504L;

  private String gaid;
  
  private List<UnitreeBarFilterDto> filter;
}
