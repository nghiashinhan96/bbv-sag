package com.sagag.services.service.request.invoice;

import com.sagag.services.article.api.request.InvoiceExternalSearchRequest;
import com.sagag.services.common.utils.DateUtils;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class InvoiceSearchRequest implements Serializable {

  private static final long serialVersionUID = -1357221120785841456L;

  private String dateFrom;

  private String dateTo;

  private Boolean oldInvoice;

  private static String getUTCDateFrom(String dateFrom) {
    return StringUtils.isBlank(dateFrom) ? StringUtils.EMPTY
        : dateFrom + DateUtils.T + DateUtils.BEGIN_OF_DAY + DateUtils.Z;
  }

  private static String getUTCDateTo(String dateTo) {
    return StringUtils.isBlank(dateTo) ? StringUtils.EMPTY
        : dateTo + DateUtils.T + DateUtils.END_OF_DAY + DateUtils.Z;
  }

  public InvoiceExternalSearchRequest toAxInvoiceSearchRequest() {
    return InvoiceExternalSearchRequest.builder().dateFrom(getUTCDateFrom(dateFrom))
        .dateTo(getUTCDateTo(dateTo)).build();
  }
}
