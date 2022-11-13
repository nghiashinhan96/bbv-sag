package com.sagag.services.gtmotive.dto.request.mainmoduleservice;

import com.sagag.services.gtmotive.domain.response.EquipmentRank;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class GtmotivePartsListSearchRequest implements Serializable {

  private static final long serialVersionUID = 1758199269477646155L;
  String umc;
  List<String> equipments;
  List<EquipmentRank> equipmentRanks;
}
