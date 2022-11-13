package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class SecurityCodeRequestDto implements Serializable {

  private static final long serialVersionUID = -4358760186457135451L;

  private String username;
  private String affiliateId;
  private String langCode;
  private String redirectUrl;
  private String invoiceRecipientEmail;

  @JsonIgnore
  private boolean digiInvoiceRequest = false;
  
  public String buildRedirectUrl(String code, String hashUsernameCode) {
    StringBuilder strBuilder = new StringBuilder(this.redirectUrl);
    strBuilder.append("?code=").append(code).append("&reg=").append(hashUsernameCode);
    return strBuilder.toString();
  }

}
