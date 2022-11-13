package com.sagag.services.oates.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OatesVehicleDto implements Serializable {

  private static final long serialVersionUID = -2020327837629988629L;

  private String href;

  private String vehicleName;

}
