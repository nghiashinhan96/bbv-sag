package com.sagag.services.domain.eshop.tour.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WssTourSearchRequestCriteria implements Serializable {

  private static final long serialVersionUID = 7984845448190711772L;

  private Boolean orderDescByTourName;

  @JsonIgnore
  private Integer orgId;
}