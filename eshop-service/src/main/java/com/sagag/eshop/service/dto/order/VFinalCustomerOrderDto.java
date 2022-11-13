package com.sagag.eshop.service.dto.order;

import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.VFinalCustomerOrder;
import com.sagag.services.common.contants.SagConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Final customer order dto class.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VFinalCustomerOrderDto implements Serializable {

  private static final long serialVersionUID = 7690931548195470097L;

  private Long id;
  private Long orgId;
  private Date orderDate;
  private Double totalGrossPrice;
  private String companyName;
  private String address;
  private String postcode;
  private String customerNumber;
  private String vehicleDescs;
  private String articleDesc;
  private String username;
  private String status;
  private String reference;
  private String branchRemark;
  private String positionReferences;
  private Double totalFinalCustomerNetPrice;

  private Double totalGrossPriceWithVat;

  private Double totalFinalCustomerNetPriceWithVat;

  public VFinalCustomerOrderDto(VFinalCustomerOrder vFinalCustomerOrder) {
    this.id = vFinalCustomerOrder.getId();
    this.orgId = vFinalCustomerOrder.getOrgId();
    this.orderDate = vFinalCustomerOrder.getDate();
    this.companyName = vFinalCustomerOrder.getCompanyName();
    this.address = vFinalCustomerOrder.getAddress();
    this.postcode = vFinalCustomerOrder.getPostcode();
    this.customerNumber = vFinalCustomerOrder.getCustomerNumber();
    this.totalGrossPrice = vFinalCustomerOrder.getTotalGrossPrice();
    this.vehicleDescs = buildDisplayedVehicleDesc(vFinalCustomerOrder.getVehicleDescs());
    this.articleDesc = vFinalCustomerOrder.getArticleDesc();
    this.username = vFinalCustomerOrder.getUsername();
    this.status = vFinalCustomerOrder.getStatus();
    this.reference = vFinalCustomerOrder.getReference();
    this.branchRemark = vFinalCustomerOrder.getBranchRemark();
    this.positionReferences = vFinalCustomerOrder.getPositionReferences();
    this.totalFinalCustomerNetPrice = vFinalCustomerOrder.getTotalFinalCustomerNetPrice();
    this.totalGrossPriceWithVat = vFinalCustomerOrder.getTotalGrossPriceWithVat();
    this.totalFinalCustomerNetPriceWithVat = vFinalCustomerOrder.getTotalFinalCustomerNetPriceWithVat();
  }

  public VFinalCustomerOrderDto(FinalCustomerOrder finalCustomerOrder) {
    this.id = finalCustomerOrder.getId();
    this.orgId = finalCustomerOrder.getOrgId();
    this.orderDate = finalCustomerOrder.getDate();
    this.totalGrossPrice = finalCustomerOrder.getTotalGrossPrice();
    this.totalFinalCustomerNetPrice = finalCustomerOrder.getTotalFinalCustomerNetPrice();
    this.totalGrossPriceWithVat = finalCustomerOrder.getTotalGrossPriceWithVat();
    this.totalFinalCustomerNetPriceWithVat = finalCustomerOrder.getTotalFinalCustomerNetPriceWithVat();
  }

  private String buildDisplayedVehicleDesc(String vehicleDescs) {
    if (StringUtils.isEmpty(vehicleDescs)) {
      return StringUtils.EMPTY;
    }
    Collection<String> result = Arrays.asList(vehicleDescs.split(SagConstants.SEMICOLON));
    return result.stream().distinct()
        .collect(Collectors.joining(SagConstants.HYPHEN_HAS_SPACE_DELIMITER));
  }

}
