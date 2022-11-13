package com.sagag.services.service.request.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;


/**
 * Request body class for viewing order histories.
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OrderHistoryDetailRequestBody implements Serializable {

  private static final long serialVersionUID = 2218707380252191313L;

  private Long orderId;
  private String orderNumber;
  private String affiliate;
  private String customerNr;
  private String basketItemSourceId;
  private String basketItemSourceDesc;
}
