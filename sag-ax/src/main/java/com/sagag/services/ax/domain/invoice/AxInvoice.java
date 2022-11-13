package com.sagag.services.ax.domain.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class AxInvoice implements Serializable {

  private static final long serialVersionUID = 9199139835305989628L;

  private String invoiceNr;

  private Date invoiceDate;

  private String name;

  private String customerNr;

  private String zipcode;

  private String city;

  private String country;

  private String termOfPayment;

  private Double amount;

  private String paymentType;

  @JsonIgnore
  public InvoiceDto toDto() {
    return SagBeanUtils.map(this, InvoiceDto.class);
  }
}

