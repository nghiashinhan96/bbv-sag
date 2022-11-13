package com.sagag.services.domain.eshop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FinalCustomerOrderDetailDto implements Serializable{

  private static final long serialVersionUID = 9188561232287544051L;

  private Long id;

}
