package com.sagag.eshop.service.exception;

import lombok.NoArgsConstructor;

import javax.validation.ValidationException;

@NoArgsConstructor
public class VehicleNotFoundException extends ValidationException {

  private static final long serialVersionUID = -6535636312860139076L;

  public VehicleNotFoundException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return "VEHICLE_NOT_FOUND";
  }
}
