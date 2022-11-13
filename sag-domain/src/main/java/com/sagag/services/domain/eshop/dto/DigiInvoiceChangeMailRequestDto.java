package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
public class DigiInvoiceChangeMailRequestDto implements Serializable {

  private static final long serialVersionUID = -1072665713870149418L;

  // security code
  private String token;

  private String hashUsernameCode;

  private String invoiceRecipientEmail;

  private String invoiceRequestEmail;

}
