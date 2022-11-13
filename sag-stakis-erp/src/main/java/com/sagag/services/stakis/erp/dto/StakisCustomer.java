package com.sagag.services.stakis.erp.dto;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StakisCustomer extends Customer {

  private static final long serialVersionUID = -1101340467674448270L;

  @JsonIgnore
  private String id;

  private String closetTour;

  @Override
  public String getNr() {
    return StringUtils.defaultString(this.id);
  }

}
