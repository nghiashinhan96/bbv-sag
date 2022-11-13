package com.sagag.services.domain.sag.erp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationAvailability implements Serializable {

  private static final long serialVersionUID = 1081742621905428305L;

  private String state;
  private String name;
  private boolean allInPrioLocations;
  private List<LocationAvailabilityItem> items;

}
