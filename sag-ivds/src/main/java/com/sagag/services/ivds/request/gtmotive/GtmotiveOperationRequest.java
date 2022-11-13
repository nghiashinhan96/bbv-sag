package com.sagag.services.ivds.request.gtmotive;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GtmotiveOperationRequest implements Serializable {

  private static final long serialVersionUID = 1127828162572695441L;

  private String makeCode;

  private String estimateId;

  private String vin;

  private String vehicleCode;

  private List<GtmotiveOperationItem> operations;

  private String vehicleId;

  private List<String> cupis;

  @JsonIgnore
  private boolean usingVersion2;
}
