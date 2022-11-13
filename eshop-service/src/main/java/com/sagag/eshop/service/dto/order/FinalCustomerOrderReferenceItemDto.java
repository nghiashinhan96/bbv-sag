package com.sagag.eshop.service.dto.order;

import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.services.common.contants.SagConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinalCustomerOrderReferenceItemDto implements Serializable {

  private static final long serialVersionUID = 2051761494690914421L;

  private String vehicleId;

  private String articleId;

  private String reference;

  public FinalCustomerOrderReferenceItemDto(FinalCustomerOrderItem finalCustomerOrderItem) {
    vehicleId = StringUtils.defaultString(finalCustomerOrderItem.getVehicleId(),
        SagConstants.KEY_NO_VEHICLE);
    articleId = finalCustomerOrderItem.getArticleId();
    reference = finalCustomerOrderItem.getReference();
  }
}
