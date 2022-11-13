package com.sagag.services.gtmotive.domain.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class AbstractGtmotiveCriteria extends GtmotiveVinDecodeCriteria implements Serializable {

  private static final long serialVersionUID = -159275924557752463L;

  private String estimateId;

  private boolean updateEstimateIdMode;

  public abstract boolean isVinRequest();
}
