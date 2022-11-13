package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "responseInfo")
public class GtVehicleInfoResponseInfo {

  private GtVehicleInfoDec vehicleInfoDec;

}
