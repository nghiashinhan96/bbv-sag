package com.sagag.services.domain.eshop.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SytemAdminChangePasswordDto implements Serializable {

  private static final long serialVersionUID = 696409138427558322L;

  private Long userId;
  private String password;
  private String redirectUrl;

}
