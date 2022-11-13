package com.sagag.services.domain.sag.erp;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationAvailabilityItem implements Serializable {

  private static final long serialVersionUID = -9132306440552163487L;

  private String locationId;
  private String locationName;
  private String locationType;
  private String locationPhoneNr;
  private Double quantity;
}
