package com.sagag.eshop.service.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryFilterDto implements Serializable {

  private static final long serialVersionUID = 8998105363473415071L;

  private String status;
  private String from;
  private String to;
  private String username;
  private String articleNumber;
  private String orderNumber;

}

