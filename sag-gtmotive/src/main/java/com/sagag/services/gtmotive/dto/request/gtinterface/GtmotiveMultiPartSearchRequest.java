package com.sagag.services.gtmotive.dto.request.gtinterface;

import lombok.Data;

import java.io.Serializable;

@Data
public class GtmotiveMultiPartSearchRequest implements Serializable {

  private static final long serialVersionUID = 1L;
  private GtmotivePartUpdateRequest partUpdateRequest;
  private GtmotivePartInfoRequest partInfoRequest;
}
