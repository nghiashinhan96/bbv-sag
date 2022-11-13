package com.sagag.services.gtmotive.exception;

import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

@Getter
public class GtmotiveNotFoundVehicleException extends ValidationException {

  private static final long serialVersionUID = 827801227455594209L;

  public GtmotiveNotFoundVehicleException(String message) {
    super(message);
  }

  @Override
  public String getCode() {
    return "GT_VH_NF_001";
  }

  @Override
  public String getKey() {
    return "NOT_FOUND_VEHICLE";
  }
}
