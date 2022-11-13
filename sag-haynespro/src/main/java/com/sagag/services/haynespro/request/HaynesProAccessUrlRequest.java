package com.sagag.services.haynespro.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.enums.HaynesProLicenseTypeEnum;
import com.sagag.services.common.utils.SagCollectionUtils;

import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HaynesProAccessUrlRequest implements Serializable {

  private static final long serialVersionUID = 1317572147546182997L;

  private String username;

  // Generate by user key.
  private String uuid;

  private String language;

  private String subject;

  private HaynesProLicenseTypeEnum licenseType;

  private String optionalParameters;

  private String hourlyRate;

  private String vatRate;

  @JsonProperty("kType")
  private String kType;

  private String motorId;

  private String callbackUrl;

  private String callbackBtnText;

  private String currencyCode;

  public String defaultLicenseType() {
    return Optional.ofNullable(getLicenseType())
      .map(HaynesProLicenseTypeEnum::getCode)
      .orElse(HaynesProLicenseTypeEnum.ULTIMATE.getCode());
  }

  public String getOptionalParameterByKeyOrDefault(String key) {
    return SagCollectionUtils.convertOptionalParametersStringToMap(this.optionalParameters)
        .getOrDefault(key, defaultLicenseType());
  }

}
