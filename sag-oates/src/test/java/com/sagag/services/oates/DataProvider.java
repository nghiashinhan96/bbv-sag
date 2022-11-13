package com.sagag.services.oates;

import java.util.Collections;
import java.util.List;

import com.sagag.services.oates.domain.OatesRecommendVehicles;
import com.sagag.services.oates.domain.OatesVehicleEquipment;
import com.sagag.services.oates.domain.OatesVehicleEquipmentInfo;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DataProvider {

  public static final String VEHICLE_ID = "V124808M32594";
  public static final String KTYPE = "124808";
  public static final String HREF =
      "/equipment/a7_sportback_4ga_4gf_2_8_fsi_quattro_162kw_FawEcBR7Y";
  public static final String NAME =
      "A7 Sportback (4GA, 4GF) 2.8 FSI quattro 162kW (B) (2014-2018)";

  public static final String[] OATES_APP_TYPE_ORIGINALS = {
      "Brake and Clutch", "Coolant", "Differential", "Engine", "Greases", "Hydraulic Fluids",
      "Other", "Steering", "Transmission"
  };

  public OatesRecommendVehicles buildNullOatesVehicles() {
    return buildOatesVehicles(null);
  }

  public OatesRecommendVehicles buildEmptyOatesVehicles() {
    return buildOatesVehicles(buildOatesVehicleEquipment(Collections.emptyList()));
  }

  public OatesRecommendVehicles buildOatesVehicles(OatesVehicleEquipment equipment) {
    OatesRecommendVehicles vehicles = new OatesRecommendVehicles();
    vehicles.setEquipmentList(equipment);
    return vehicles;
  }

  public OatesVehicleEquipment buildOatesVehicleEquipment(
      List<OatesVehicleEquipmentInfo> equipments) {
    OatesVehicleEquipment equipment = new OatesVehicleEquipment();
    equipment.setEquipment(equipments);
    return equipment;
  }

}
