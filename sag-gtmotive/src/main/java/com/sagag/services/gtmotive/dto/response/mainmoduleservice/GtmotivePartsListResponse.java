package com.sagag.services.gtmotive.dto.response.mainmoduleservice;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class GtmotivePartsListResponse {

  private List<GtmotivePart> parts;

  public static GtmotivePartsListResponse empty() {
    GtmotivePartsListResponse emptyResponse = new GtmotivePartsListResponse();
    emptyResponse.setParts(Collections.emptyList());
    return emptyResponse;
  }
}
