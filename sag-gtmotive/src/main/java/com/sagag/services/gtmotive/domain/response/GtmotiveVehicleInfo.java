package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GtmotiveVehicleInfo implements Serializable {

  private static final long serialVersionUID = -648289392508419247L;

  private GtmotiveUmc umc;

  private GtmotiveMakeCode makeCode;

  private GtmotiveMake make;

  private GtmotiveModel model;

  private RegistrationNumber registrationNr;

  private GtmotiveVin vin;

  private Integer commercialModelId;

  private List<EquipmentItem> equipments;

  private List<EquipmentItem> ranks;

  private String modelType;

}
