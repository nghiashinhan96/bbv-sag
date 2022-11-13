package com.sagag.services.service.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.service.order.model.OrderRequestType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * Class to contain order request with additional info request to send to ERP.
 *
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderRequestBody implements Serializable {

  private static final long serialVersionUID = -5391051662864784903L;

  @NonNull
  private ExternalOrderRequest orderRequest;

  /**
   * This field reused when loaded in order history.
   */
  private Double totalWithVAT;

  /**
   * To let user get which order belongs to sale.
   */
  private Long saleId;

  @JsonIgnore
  private String timezone;

  @JsonIgnore
  private OrderRequestType orderRequestType;

}
