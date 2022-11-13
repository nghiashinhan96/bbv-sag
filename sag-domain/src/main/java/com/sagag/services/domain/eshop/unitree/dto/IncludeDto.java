package com.sagag.services.domain.eshop.unitree.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
// @formatter:off
@JsonPropertyOrder({"idDlnr", "genarts", "artgrp", "artids", "brands", "criteria"})
// @formatter:on
public class IncludeDto implements Serializable {

  private static final long serialVersionUID = -8787528976850027966L;

  private String genarts;

  private String artids;

  private String brands;

  private String idDlnr;

  private String artgrp;
  
  private List<CriteriaDto> criteria;
}
