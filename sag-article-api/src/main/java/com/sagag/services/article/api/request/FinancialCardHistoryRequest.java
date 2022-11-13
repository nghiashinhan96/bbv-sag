package com.sagag.services.article.api.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FinancialCardHistoryRequest implements Serializable {

  private static final long serialVersionUID = -1739415600617278383L;

  private String paymentMethod;

  private String sorting;

  private String dateFrom;

  private String dateTo;

  private String status;

  private int page;

}
