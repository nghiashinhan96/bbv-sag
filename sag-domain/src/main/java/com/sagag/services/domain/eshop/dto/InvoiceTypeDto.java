package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceTypeDto implements Serializable {
  private static final long serialVersionUID = -7566784349145413349L;

  private int id;

  private String descCode;

  private String invoiceType;

  private String invoiceTypeDesc;

  private boolean allowChoose;
}
