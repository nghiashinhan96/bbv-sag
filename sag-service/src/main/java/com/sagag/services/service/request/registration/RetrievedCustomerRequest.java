package com.sagag.services.service.request.registration;

import lombok.Data;

import java.io.Serializable;

@Data
public class RetrievedCustomerRequest implements Serializable {

  private static final long serialVersionUID = 8604777511651483847L;

  private String customerNumber;

  private String affiliate;

  private String postCode;

}
