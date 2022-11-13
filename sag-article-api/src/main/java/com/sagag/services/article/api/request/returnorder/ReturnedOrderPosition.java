package com.sagag.services.article.api.request.returnorder;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ReturnedOrderPosition implements Serializable {

  private static final long serialVersionUID = -1842122327524167404L;

  private String transactionId;

  private Integer quantity;

  private String reasonCode;

  private boolean quarantine;

  private String quarantineReason;

  private String axPaymentType;

  private String cashDiscount;
}
