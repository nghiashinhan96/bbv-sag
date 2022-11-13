package com.sagag.services.service.request.offer;

import lombok.Data;

import java.io.Serializable;

@Data
public class OfferPersonSearchRequestBody implements Serializable {

  private static final long serialVersionUID = 4123819450934605974L;

  private String name;

  private String address;

  private String contactInfo;

  private Boolean orderDescByName;

  private Boolean orderDescByAddress;

  private Boolean orderDescByContactInfo;

}
