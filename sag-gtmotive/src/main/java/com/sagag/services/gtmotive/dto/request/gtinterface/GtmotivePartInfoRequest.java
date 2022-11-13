package com.sagag.services.gtmotive.dto.request.gtinterface;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class GtmotivePartInfoRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String estimateId;
  String umc;
  List<String> equipments;
}
