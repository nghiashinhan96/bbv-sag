package com.sagag.services.gtmotive.dto.response.gtinterface;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@JsonPropertyOrder({ "url", "estimateId", "errorMessage", "errorCode", "vinLogId" })
public class GtmotiveVinSecurityCheckResponse implements Serializable {

  private static final long serialVersionUID = 1646632434481464734L;

  private static final int VIN_MAKECODE_UNSUPPORTED = 100;

  private String url;
  private String estimateId;
  private String errorMessage;
  private Integer errorCode;
  private String vinLogId;
  private String vin;

  public static GtmotiveVinSecurityCheckResponse unSupportedVinMakeCode(String makeCode) {
    GtmotiveVinSecurityCheckResponse response = new GtmotiveVinSecurityCheckResponse();
    response.setErrorCode(VIN_MAKECODE_UNSUPPORTED);
    response.setErrorMessage(StringUtils.defaultString(makeCode));
    return response;
  }
}
