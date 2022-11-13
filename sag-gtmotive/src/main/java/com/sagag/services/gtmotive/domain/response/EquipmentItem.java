package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class EquipmentItem implements Serializable {

  private static final long serialVersionUID = 6924503956454016576L;

  private String value;
  private String description;
  private String family;
  private String familyDescription;
  private String subFamily;
  private String subFamilyDescription;
  private String source;
}
