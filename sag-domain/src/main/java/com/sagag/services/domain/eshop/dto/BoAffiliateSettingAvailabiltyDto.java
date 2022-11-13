package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class BoAffiliateSettingAvailabiltyDto implements Serializable{

  private static final long serialVersionUID = -90971111879304960L;

  private List<SettingLanguageDto> detailAvailText;
  
  private List<SettingLanguageDto> listAvailText;
  
  private Boolean availIcon;
  
  private Boolean dropShipmentAvail;
  
  @JsonIgnore
  private List<String> supportedLanguagesIso;
  
  public SettingLanguageDto getDetailAvailTextByLanguage(String lang) {
    return detailAvailText.stream().filter(t -> t.getLangIso().equals(lang)).findFirst().orElse(new SettingLanguageDto());
  }
  
  public SettingLanguageDto getListAvailTextByLanguage(String lang) {
    return listAvailText.stream().filter(t -> t.getLangIso().equals(lang)).findFirst().orElse(new SettingLanguageDto());
  }
  
}
