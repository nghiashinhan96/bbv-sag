package com.sagag.services.gtmotive.dto.response.mainmoduleservice;

import lombok.Data;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collections;
import java.util.List;

@Data
public class GtmotivePartsThreeResponse {

  private List<GtmotivePartsThreeReference> references;
  private Integer errorCode;

  public static GtmotivePartsThreeResponse empty() {
    GtmotivePartsThreeResponse emptyResponse = new GtmotivePartsThreeResponse();
    emptyResponse.setReferences(Collections.emptyList());
    emptyResponse.setErrorCode(NumberUtils.INTEGER_MINUS_ONE);
    return emptyResponse;
  }
}
