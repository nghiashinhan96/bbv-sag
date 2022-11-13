package com.sagag.services.service.request.offer;

import lombok.Data;

import java.io.Serializable;

@Data
public class OfferPersonRequestBody implements Serializable {

  private static final long serialVersionUID = 4971892506464640601L;

  private String salutation;

  private String companyName;

  private String firstName;

  private String lastName;

  private String road;

  private String additionalAddress1;

  private String additionalAddress2;

  private String poBox;

  private String postCode;

  private String place;

  private String phone;

  private String fax;

  private String email;
}
