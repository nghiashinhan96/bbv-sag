package com.sagag.services.elasticsearch.criteria;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Class to define the criteria to vehicle filtering.
 */
@Data
@Builder
public class VehicleFilteringTerms implements Serializable {

  private static final long serialVersionUID = -2558321427674953106L;

  private String vehMake;

  private String vehModel;

  private Integer vehYearFrom;

  private Integer vehYearTo;

  private String vehFuel;

  private Integer vehPower;

  private String vehBody;

}
