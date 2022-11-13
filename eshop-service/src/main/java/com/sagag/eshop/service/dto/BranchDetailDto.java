package com.sagag.eshop.service.dto;

import com.sagag.eshop.repo.entity.Branch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BranchDetailDto extends BranchDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  private String addressStreet;

  private String addressCity;

  private String addressDesc;

  private String addressCountry;

  private Integer countryId;

  private String zip;

  private Integer orgId;

  private String regionId;

  private String primaryPhone;

  private String primaryFax;

  private String primaryEmail;

  private Boolean validForKSL;

  private String primaryUrl;

  private Boolean hideFromCustomers;

  private Boolean hideFromSales;

  public BranchDetailDto(final Branch branch) {
    super(branch);
    this.addressStreet = branch.getAddressStreet();
    this.addressCity = branch.getAddressCity();
    this.addressDesc = branch.getAddressDesc();
    this.addressCountry = branch.getAddressCountry();
    this.countryId = branch.getCountryId();
    this.zip = branch.getZip();
    this.orgId = branch.getOrgId();
    this.regionId = branch.getRegionId();
    this.primaryPhone = branch.getPrimaryPhone();
    this.primaryFax = branch.getPrimaryFax();
    this.primaryEmail = branch.getPrimaryEmail();
    this.validForKSL = branch.getValidForKSL();
    this.primaryUrl = branch.getPrimaryUrl();
    this.hideFromCustomers = branch.getHideFromCustomers();
    this.hideFromSales = branch.getHideFromSales();
  }
}
