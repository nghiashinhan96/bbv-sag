package com.sagag.services.domain.eshop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SytemAdminResetPasswordDto implements Serializable {

  private static final long serialVersionUID = 1L;
  private String email;
  private String redirectUrl;
}
