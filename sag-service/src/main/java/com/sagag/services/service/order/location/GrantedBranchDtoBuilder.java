package com.sagag.services.service.order.location;

import com.sagag.services.common.enums.LocationType;
import com.sagag.services.common.profiles.SbProfile;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.GrantedBranch;
import com.sagag.services.hazelcast.api.BranchCacheService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@SbProfile
public class GrantedBranchDtoBuilder {

  @Autowired
  private BranchCacheService branchCacheService;

  public List<GrantedBranchDto> buildWtGrantedBranches(List<GrantedBranch> grantedBranches,
      String companyName) {
    return CollectionUtils.emptyIfNull(grantedBranches).stream()
        .map(branch -> from(branch, companyName)).collect(Collectors.toList());
  }

  public GrantedBranchDto from(GrantedBranch branch, String companyName) {
    Assert.notNull(branch, "branch should not be null");
    Assert.hasText(companyName, "companyName should not be empty");
    Map<String, String> branchNameMap = branchCacheService.getCachedBranches(companyName).stream()
        .collect(Collectors.toMap(CustomerBranch::getBranchId, CustomerBranch::getBranchName));

    return GrantedBranchDto.builder()
        .branchId(branch.getBranchId())
        .branchName(branchNameMap.get(branch.getBranchId()))
        .paymentMethodAllowed(branch.getPaymentMethodAllowed())
        .locationType(LocationType.fromOrderingPriority(branch.getOrderingPriority()).name())
        .build();
  }

}
