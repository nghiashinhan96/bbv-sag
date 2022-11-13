package com.sagag.services.service.response.gtmotive;

import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionBaseItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GtmotiveEquipmentOptionsDto implements Serializable {

  private static final long serialVersionUID = 3109635744939193394L;
  private GtmotiveEquipmentOptionBaseItem applicability;
  private String manufacturerCode;
  private String incompatibilityGroup;
  private List<String> incompatibilityGroupList;
}
