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
@XmlRootElement(name = "request")
public class GtVehicleInfoRequest {

  private AuthenticationData authenticationData;

  private GtVehicleRequestInfo requestInfo;

  @Override
  public String toString() {
    return "ClassPojo [authenticationData = " + authenticationData + ", requestInfo = "
        + requestInfo + "]";
  }
}
