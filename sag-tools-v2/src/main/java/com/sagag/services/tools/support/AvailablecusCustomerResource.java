package com.sagag.services.tools.support;

import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;

import lombok.Value;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class AvailablecusCustomerResource implements Serializable {

  private static final long serialVersionUID = -4663355612717764381L;

  private List<MappingUserIdEblConnect> allowedUseOfferCustomerList;


  public List<Long> getEblOrgIdList() {
    if (CollectionUtils.isEmpty(allowedUseOfferCustomerList)) {
      return Collections.emptyList();
    }
    return allowedUseOfferCustomerList.stream().map(MappingUserIdEblConnect::getEblOrgId)
      .collect(Collectors.toList());
  }
}
