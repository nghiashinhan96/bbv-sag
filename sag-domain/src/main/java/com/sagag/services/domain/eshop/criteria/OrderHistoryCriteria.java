package com.sagag.services.domain.eshop.criteria;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderHistoryCriteria implements Serializable {

  private static final long serialVersionUID = -3022210442319017862L;

  private String orderStatus;

  private String username;

  private String dateFrom;

  private String dateTo;

}
