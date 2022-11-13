package com.sagag.services.gtmotive.dto.request.mainmoduleservice;

import com.sagag.services.gtmotive.domain.response.EquipmentRank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GtmotivePartsThreeSearchRequest implements Serializable {

  private static final long serialVersionUID = 1L;
  String partCode;
  String functionalGroup;
  String umc;
  List<String> equipments;
  List<EquipmentRank> equipmentRanks;
}
