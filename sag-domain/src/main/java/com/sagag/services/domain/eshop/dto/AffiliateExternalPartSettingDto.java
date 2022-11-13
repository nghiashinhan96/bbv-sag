package com.sagag.services.domain.eshop.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateExternalPartSettingDto implements Serializable {

  private static final long serialVersionUID = -7166572643607979933L;

  private boolean useExternalParts;
  private boolean showInReferenceGroup;

  private List<SettingLanguageDto> headerNames;
  private List<SettingLanguageDto> orderTexts;
}
