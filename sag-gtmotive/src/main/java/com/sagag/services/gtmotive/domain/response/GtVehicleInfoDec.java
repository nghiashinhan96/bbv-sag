package com.sagag.services.gtmotive.domain.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "vehicleInfoDec")
public class GtVehicleInfoDec {

  private EquipmentList equipmentList;

  private String vin;

  private ModelList modelList;

  public EquipmentList getEquipmentList() {
    return equipmentList;
  }

  public void setEquipmentList(EquipmentList equipmentList) {
    this.equipmentList = equipmentList;
  }

  public String getVin() {
    return vin;
  }

  public void setVin(String vin) {
    this.vin = vin;
  }

  public ModelList getModelList() {
    return modelList;
  }

  public void setModelList(ModelList modelList) {
    this.modelList = modelList;
  }

  @Override
  public String toString() {
    return "ClassPojo [equipmentList = " + equipmentList + ", vin = " + vin + ", modelList = "
        + modelList + "]";
  }
}
