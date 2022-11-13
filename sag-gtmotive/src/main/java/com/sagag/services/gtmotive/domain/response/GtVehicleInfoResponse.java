package com.sagag.services.gtmotive.domain.response;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
public class GtVehicleInfoResponse {

  private GtVehicleInfoResponseInfo responseInfo;

  public String getMakeCode() {
    if (responseInfo == null || responseInfo.getVehicleInfoDec() == null
      || responseInfo.getVehicleInfoDec().getModelList() == null
      || responseInfo.getVehicleInfoDec().getModelList().getModelData() == null) {
      return StringUtils.EMPTY;
    }
    return responseInfo.getVehicleInfoDec().getModelList()
      .getModelData().getMakeCode();
  }

}
