package com.sagag.services.gtmotive.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class GtVehicleInfo {

  private String vin;

  @Override
  public String toString() {
    return "ClassPojo [vin = " + vin + "]";
  }
}
