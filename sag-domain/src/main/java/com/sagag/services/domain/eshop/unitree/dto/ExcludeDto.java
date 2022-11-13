package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
//@formatter:off
@JsonPropertyOrder({
  "idDlnr",
  "genarts",
  "artgrp",
  "artids",
  "brands",
  "criteria"
})
//@formatter:on
public class ExcludeDto implements Serializable {

  private static final long serialVersionUID = 5071166002587949343L;

  private String genarts;

  private String artids;

  private String brands;

  private String idDlnr;

  private String artgrp;

  private List<CriteriaDto> criteria;
}
