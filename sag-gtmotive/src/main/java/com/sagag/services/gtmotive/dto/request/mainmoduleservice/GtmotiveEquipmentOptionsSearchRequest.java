package com.sagag.services.gtmotive.dto.request.mainmoduleservice;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GtmotiveEquipmentOptionsSearchRequest {

  private String umc;
  private List<String> partCodes;
  private String functionalGroup;
}
