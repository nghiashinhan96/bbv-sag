package com.sagag.services.incentive.response;

import com.sagag.services.incentive.linkgenerator.impl.IncentiveMode;

import lombok.Data;

@Data
public class IncentiveLinkResponse {

  private String url;

  private IncentiveMode mode;

  private String description;

  private Long points;

  private boolean showHappyPoints;

  private boolean acceptHappyPointTerm;
}
