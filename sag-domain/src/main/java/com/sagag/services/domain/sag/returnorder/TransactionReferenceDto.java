package com.sagag.services.domain.sag.returnorder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.common.enums.PaymentMethodType;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class TransactionReferenceDto implements Serializable {

  private static final long serialVersionUID = -1166818303187618489L;

  private String transId;

  private String articleId;

  private String articleName;

  private String articleKeyword;

  private String orderNr;

  private String customerNr;

  private String customerName;

  private Integer quantity;

  private Integer returnQuantity;

  private String branchId;

  private String axPaymentType;

  private PaymentMethodType paymentType;

  private String termOfPayment;

  private String cashDiscount;

  private String unitOfMeasurement;

  private String sourcingType;

  private boolean editable;

  private List<TransactionReferenceDto> attachedTransactionReferences;

}
