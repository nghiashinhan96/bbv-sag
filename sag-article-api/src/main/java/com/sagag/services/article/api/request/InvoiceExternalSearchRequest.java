package com.sagag.services.article.api.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InvoiceExternalSearchRequest implements Serializable {

  private static final long serialVersionUID = -1357221120785841456L;

  private String dateFrom;

  private String dateTo;

  private String orderNr;

}
