package com.sagag.services.incentive.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Incentives point POJO.
 */
@Data
@Builder
@AllArgsConstructor
public class IncentivePointsDto implements Serializable {

  private static final long serialVersionUID = -8100680323436012485L;

  private Long points;

  private boolean showHappyPoints;

  private boolean acceptHappyPointTerm;

}
