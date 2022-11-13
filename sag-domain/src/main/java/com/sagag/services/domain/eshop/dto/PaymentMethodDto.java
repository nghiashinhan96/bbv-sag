package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMethodDto implements Serializable {

  private static final long serialVersionUID = 4408012446643405416L;

  private int id;

  private String descCode;

  private String payMethod;

  private String description;

  private boolean allowChoose;
}
