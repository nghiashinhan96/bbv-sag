package com.sagag.services.gtmotive.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.commons.lang3.StringUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class GtmotiveVehicleCriteria extends AbstractGtmotiveCriteria {

  private static final long serialVersionUID = -4151262850736361457L;

  private String umc;

  private String gtMod;

  private String gtEng;

  private String gtDrv;

  @JsonIgnore
  public boolean isVinMode() {
    return !StringUtils.isBlank(getModifiedVin());
  }

  @JsonIgnore
  public boolean isServiceScheduleMode() {
    return StringUtils.isNoneBlank(umc, gtMod, gtEng, gtDrv);
  }

  @JsonIgnore
  public boolean isValidMode() {
    return isVinMode() || isServiceScheduleMode();
  }

  @Override
  public boolean isVinRequest() {
    return isVinMode();
  }

}
