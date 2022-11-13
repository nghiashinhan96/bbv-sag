package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "id", "password" })
public class EshopUserLoginDto implements Serializable {

  private static final long serialVersionUID = 8354538600195795425L;

  private Long id;
  private String oldPassword;
  private String password;
  private String redirectUrl;
  private String affiliate;
  private String langCode;

  @JsonProperty("isFinalUser")
  private boolean isFinalUser;

  // security code
  private String token;

  private String hashUsernameCode;

}
