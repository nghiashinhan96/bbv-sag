package com.sagag.services.service.request.order;

import com.sagag.services.domain.eshop.dto.GrantedBranchDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderConditionByLocation {

  private GrantedBranchDto location;
  
  private GrantedBranchDto pickupLocation;
  
}
