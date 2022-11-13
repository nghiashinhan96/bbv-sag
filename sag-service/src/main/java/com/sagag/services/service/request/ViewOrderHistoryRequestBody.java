package com.sagag.services.service.request;

import lombok.Data;

import java.io.Serializable;


/**
 * Request body class for viewing order histories.
 */
@Data
public class ViewOrderHistoryRequestBody implements Serializable {

  private static final long serialVersionUID = 2218707380252191313L;

  private String status;
  private String from;
  private String to;
  private String username;
  private String articleNumber;
  private String orderNumber;
  private int page;
  private int size;
  private String onbehalfToken;

}
