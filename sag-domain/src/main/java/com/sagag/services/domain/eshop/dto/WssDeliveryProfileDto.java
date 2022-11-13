package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssDeliveryProfileDto implements Serializable {

  private static final long serialVersionUID = 8786648386416929082L;

  private Integer id;

  private String name;

  private String description;

  private Integer orgId;

  private WssBranchDto wssBranch;

  private List<WssDeliveryProfileToursDto> wssDeliveryProfileToursDtos;

  public String getBranchCode() {
    return wssBranch != null ? wssBranch.getBranchCode() : null;
  }

}
