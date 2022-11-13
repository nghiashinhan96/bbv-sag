package com.sagag.eshop.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HaynesProLicenseSettingDto implements Serializable {

  private static final long serialVersionUID = -3481236717085916833L;

  private String licenseType;

  private String optionalParameters;

}
