package com.sagag.services.article.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * The class to build AX change order status request to send to AX API.
 *
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusRequest implements Serializable {

  private static final long serialVersionUID = -7758466403788189351L;

  @NonNull
  private String orderNr;

  @NonNull
  private SalesOrderStatus processStatus;

}
