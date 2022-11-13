package com.sagag.services.ax.domain.returnorder;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;

import lombok.Data;

@Data
public class AxTransactionReference implements Serializable {

  private static final long serialVersionUID = -1166818303187618489L;

  private String transId;

  private String articleId;

  private String articleName;

  private String articleKeyword;

  private String orderNr;

  private String customerNr;

  private String customerName;

  private Integer quantity;

  @JsonProperty("returnQty")
  private Integer returnQuantity;

  private String branchId;

  @JsonProperty("paymentType")
  private String axPaymentType;

  private String termOfPayment;

  private String cashDiscount;

  private String unitOfMeasurement;

  private String sourcingType;

  @JsonIgnore
  public TransactionReferenceDto toDto() {
    TransactionReferenceDto transReferenceDto =
        SagBeanUtils.map(this, TransactionReferenceDto.class);
    transReferenceDto.setEditable(true);
    return transReferenceDto;
  }

}

