package com.sagag.eshop.service.exception;

import lombok.NoArgsConstructor;

import javax.validation.ValidationException;

@NoArgsConstructor
public class OrganisationNotFoundException extends ValidationException {

  private static final long serialVersionUID = 7369851738182820726L;

  public OrganisationNotFoundException(String msg) {
    super(msg);
  }

  @Override
  public String getMessage() {
    return "ORGANISATION_NOT_FOUND";
  }
}
