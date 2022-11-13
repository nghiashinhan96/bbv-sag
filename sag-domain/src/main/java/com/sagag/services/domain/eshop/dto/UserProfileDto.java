package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto implements Serializable {

  private static final long serialVersionUID = -4110037171609207932L;

  private long id;

  private String vinCall;

  private String license;

  @NotBlank(message = "Username is not valid")
  private String userName;

  @NotBlank(message = "Surname is not valid")
  private String surName;

  @NotBlank(message = "Firstname is not valid")
  private String firstName;

  private List<SalutationDto> salutations;

  private int salutationId;

  @Email(message = "Email is not valid")
  private String email;

  private String phoneNumber;

  private List<LanguageDto> languages;

  private int languageId;

  private List<EshopRoleDto> types;
  private int typeId;

  private String accessUrl;

  private boolean vatConfirm;

  private Boolean emailConfirmation;

  private Double hourlyRate;

  private String userType;

  @JsonInclude(Include.NON_NULL)
  private Boolean netPriceConfirm;

  @JsonInclude(Include.NON_NULL)
  private Boolean netPriceView;

  @JsonInclude(Include.NON_NULL)
  private Boolean showNetPriceEnabled;

  @JsonIgnore
  public String buildRedirectUrl(String code, String hashUsernameCode) {
    StringBuilder strBuilder = new StringBuilder(this.accessUrl);
    strBuilder.append("?code=").append(code).append("&reg=").append(hashUsernameCode);
    return strBuilder.toString();
  }
}
