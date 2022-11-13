package com.sagag.eshop.service.dto.order;

import com.sagag.eshop.repo.entity.order.FinalCustomerOrder;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FinalCustomerOrderDto implements Serializable {

  private static final long serialVersionUID = -3126982373753216752L;

  private Long id;
  private Date orderDate;
  private String reference;
  private String branchRemark;
  private Long userId;
  private Long orgId;

  private List<FinalCustomerOrderItemDto> items;

  private Double totalGrossPriceWithVat;

  private Double totalFinalCustomerNetPriceWithVat;

  public FinalCustomerOrderDto(FinalCustomerOrder finalCustomerOrder) {
    this.id = finalCustomerOrder.getId();
    this.orderDate = finalCustomerOrder.getDate();
    this.reference = finalCustomerOrder.getReference();
    this.branchRemark = finalCustomerOrder.getBranchRemark();
    this.userId = finalCustomerOrder.getUserId();
    this.orgId = finalCustomerOrder.getOrgId();
    this.totalGrossPriceWithVat = finalCustomerOrder.getTotalGrossPriceWithVat();
    this.totalFinalCustomerNetPriceWithVat = finalCustomerOrder.getTotalFinalCustomerNetPriceWithVat();
  }

  public void setItems(List<FinalCustomerOrderItem> finalCustomerOrderItems) {
    if (CollectionUtils.isEmpty(finalCustomerOrderItems)) {
      items = new ArrayList<>();
      return;
    }
    items = finalCustomerOrderItems.stream().map(FinalCustomerOrderItemDto::new)
        .collect(Collectors.toList());
  }
}
