package com.sagag.services.domain.eshop.unitree.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
// @formatter:off
@JsonPropertyOrder({
      "filter_default",
      "filter_open",
      "filter_bar",
      "filter_uom",
      "filter_caid",
      "filter_sort"
})
// @formatter:on
public class UnitreeBarFilterDto implements Serializable {

  private static final long serialVersionUID = -1938899891809938820L;

  private String filterDefault;

  private String filterOpen;

  private String filterBar;

  private String filterUom;

  private String filterCaid;

  private String filterSort;
}
