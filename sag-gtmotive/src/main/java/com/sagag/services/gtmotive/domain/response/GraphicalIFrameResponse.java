package com.sagag.services.gtmotive.domain.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@JsonPropertyOrder({ "url", "estimateId", "errorMessage", "errorCode", "vinLogId" })
public class GraphicalIFrameResponse implements Serializable {

  private static final long serialVersionUID = 1646632434481464734L;

  private static final int VIN_MAKECODE_UNSUPPORTED = 100;

  private String url;
  private String estimateId;
  private String errorMessage;
  private Integer errorCode;
  private String vinLogId;

  public static GraphicalIFrameResponse unSupportedVinMakeCode(String makeCode) {
    GraphicalIFrameResponse response = new GraphicalIFrameResponse();
    response.setErrorCode(VIN_MAKECODE_UNSUPPORTED);
    response.setErrorMessage(StringUtils.defaultString(makeCode));
    return response;
  }

}
