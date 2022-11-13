package com.sagag.services.gtmotive.dto.response.mainmoduleservice;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GtmotiveEquipmentData implements Serializable {


  private static final long serialVersionUID = 564560524150337494L;

  private GtmotiveEquipmentOptionBaseItem applicability;
  private GtmotiveEquipmentOptionBaseItem family;
  private GtmotiveEquipmentOptionBaseItem subfamily;
  private String manufacturerCode;
  private String incompatibilityGroup;
  private List<String> incompatibilityGroupList;

  @JsonIgnore
  public boolean isValidItem() {
    return family.isValidItem() && subfamily.isValidItem();
  }

  @JsonIgnore
  public String safetyGetPartCode() {
    if (Objects.isNull(applicability)) {
      return StringUtils.EMPTY;
    }
    return applicability.getCode();
  }
}
