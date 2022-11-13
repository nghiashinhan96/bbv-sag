package com.sagag.services.service.order.model;

import com.sagag.services.domain.eshop.dto.GrantedBranchDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LocationTypeFilter {

  private GrantedBranchDto location;
  
}
