package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayOption;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AffiliateAvailabilityDisplaySettingDto implements Serializable {

  private static final long serialVersionUID = -3690581622916284611L;

  private AvailabilityDisplayState availState;

  private List<SettingLanguageDto> detailAvailText;

  private List<SettingLanguageDto> listAvailText;

  private String title;

  private String description;

  private String color;

  @JsonInclude(Include.NON_NULL)
  private String confirmColor;

  private AvailabilityDisplayOption displayOption;

  @JsonIgnore
  private List<String> supportedLanguagesIso;
}
