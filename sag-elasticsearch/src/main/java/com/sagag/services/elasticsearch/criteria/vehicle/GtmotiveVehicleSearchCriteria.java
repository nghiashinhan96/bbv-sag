package com.sagag.services.elasticsearch.criteria.vehicle;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GtmotiveVehicleSearchCriteria implements Serializable {

  private static final long serialVersionUID = 7810351600714095990L;

  private String umc;

  private List<String> modelCodes;

  private List<String> engineCodes;

  private List<String> transmisionCodes;
}
