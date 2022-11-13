package com.sagag.eshop.service.dto;

import com.sagag.eshop.repo.entity.Branch;
import com.sagag.services.domain.eshop.dto.BranchOpeningTimeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class BranchDto implements Serializable {

  private static final long serialVersionUID = -8809711288178249886L;

  private Integer id;

  private Integer branchNr;

  private String branchCode;

  private String branchAddress;

  private String openingTime;

  private String closingTime;

  private String lunchStartTime;

  private String lunchEndTime;

  private boolean hideFromCustomers;

  private boolean hideFromSales;

  @Valid
  private List<BranchOpeningTimeDto> branchOpeningTimes;

  public BranchDto(final Branch branch) {
    this.id = branch.getId();
    this.branchNr = branch.getBranchNr();
    this.branchCode = branch.getBranchCode();
    this.branchAddress = branch.getAddressCity();
    this.hideFromCustomers = branch.getHideFromCustomers();
    this.hideFromSales = branch.getHideFromSales();
  }
}
