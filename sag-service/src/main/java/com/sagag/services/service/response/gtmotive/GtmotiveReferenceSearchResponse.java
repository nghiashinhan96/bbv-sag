package com.sagag.services.service.response.gtmotive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.service.request.gtmotive.GtmotiveOperationItem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class GtmotiveReferenceSearchResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String partCode;
  private List<GtmotiveOperationItem> operations;
  private List<GtmotiveEquipmentOptionsFamily> equipmentOptionFamilies;
  private String cupi;
}
