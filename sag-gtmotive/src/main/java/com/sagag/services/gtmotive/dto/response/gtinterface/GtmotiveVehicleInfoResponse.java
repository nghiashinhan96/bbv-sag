package com.sagag.services.gtmotive.dto.response.gtinterface;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.gtmotive.domain.response.GtmotiveEstimate;
import com.sagag.services.gtmotive.domain.response.GtmotiveOperation;
import com.sagag.services.gtmotive.domain.response.GtmotiveUserInfo;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonPropertyOrder({ "body" })
public class GtmotiveVehicleInfoResponse implements Serializable {

  private static final long serialVersionUID = -1563838983024229541L;

  private GtmotiveEstimate estimateId;

  private GtmotiveVehicleInfo vehicleInfo;

  private GtmotiveUserInfo userInfo;

  private List<GtmotiveOperation> operations;

  @JsonIgnore
  public boolean hasVehicleInfo() {
    return vehicleInfo != null;
  }

}
