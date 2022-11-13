package com.sagag.services.oates.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OatesVehicleEquipment implements Serializable {

  private static final long serialVersionUID = 3036040053128214412L;

  private List<OatesVehicleEquipmentInfo> equipment;

}
