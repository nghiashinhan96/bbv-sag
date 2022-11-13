package com.sagag.eshop.service.dto.finalcustomer;

import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.services.common.enums.FinalCustomerType;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

@Data
@NoArgsConstructor
public class FinalCustomerDto implements Serializable {

  private static final long serialVersionUID = -6942466039505083920L;

  private int orgId;

  private String name;

  private String description;

  private FinalCustomerType finalCustomerType;

  private int parentOrgId;

  private String addressInfo;

  private String contactInfo;

  private Boolean hasInProgressOrders;

  private FinalCustomerSettingDto customerProperties;

  public FinalCustomerDto(VFinalCustomer finalCustomer) {
    this.orgId = finalCustomer.getOrgId();
    this.name = StringUtils.defaultString(finalCustomer.getName());
    this.description = StringUtils.defaultString(finalCustomer.getDescription());
    this.parentOrgId = finalCustomer.getParentOrgId();
    this.hasInProgressOrders = finalCustomer.getHasInProgressOrders();

    Optional.ofNullable(finalCustomer.getFinalCustomerType())
    .filter(StringUtils::isNotBlank)
    .map(FinalCustomerType::valueOf)
    .ifPresent(this::setFinalCustomerType);

    setAddressInfo(finalCustomer.getAddressInfo());
    setContactInfo(finalCustomer.getContactInfo());
  }

  public String getName() {
     return StringUtils.defaultIfBlank(name, StringUtils.EMPTY);
  }

}
