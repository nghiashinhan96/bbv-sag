package com.sagag.services.article.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Data
@Setter
@AllArgsConstructor
public class FinancialCardDetailRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String paymentMethod;

  private String documentType;

  private String status;

  private int page;

}
