package com.sagag.services.gtmotive.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRank implements Serializable {

  private static final long serialVersionUID = 6514436168316085202L;

  private String value;

  private String family;

  private String subFamily;
}
