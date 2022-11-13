package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySettingMasterDataDto implements Serializable{

  private static final long serialVersionUID = -4493441138902579294L;

  private List<LanguageDto> supportedLanguages;

}
