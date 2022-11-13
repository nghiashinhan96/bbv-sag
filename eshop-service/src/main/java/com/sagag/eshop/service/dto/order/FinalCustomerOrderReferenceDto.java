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
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FinalCustomerOrderReferenceDto implements Serializable {

  private static final long serialVersionUID = -2430016642650690648L;

  private Long id;
  private String reference;
  private String branchRemark;

  private List<FinalCustomerOrderReferenceItemDto> items;

  public FinalCustomerOrderReferenceDto(FinalCustomerOrder finalCustomerOrder) {
    this.id = finalCustomerOrder.getId();
    this.reference = finalCustomerOrder.getReference();
    this.branchRemark = finalCustomerOrder.getBranchRemark();
  }

  public void setItems(List<FinalCustomerOrderItem> finalCustomerOrderItems) {
    if (CollectionUtils.isEmpty(finalCustomerOrderItems)) {
      items = new ArrayList<>();
      return;
    }
    items = finalCustomerOrderItems.stream().map(FinalCustomerOrderReferenceItemDto::new)
        .collect(Collectors.toList());
  }
}
