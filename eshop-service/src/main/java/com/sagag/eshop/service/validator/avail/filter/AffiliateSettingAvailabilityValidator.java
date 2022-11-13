package com.sagag.eshop.service.validator.avail.filter;

import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.SettingLanguageDto;
import com.sagag.services.domain.eshop.dto.BoAffiliateSettingAvailabiltyDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class AffiliateSettingAvailabilityValidator
    implements IDataValidator<BoAffiliateSettingAvailabiltyDto> {

  @Override
  public boolean validate(BoAffiliateSettingAvailabiltyDto availSetting) {
    if (!Objects.isNull(availSetting)) {

      if (CollectionUtils.isNotEmpty(availSetting.getDetailAvailText())) {
        validateAvailTextMultiLanguages(availSetting.getDetailAvailText(), availSetting.getSupportedLanguagesIso());
      }
      if (CollectionUtils.isNotEmpty(availSetting.getListAvailText())) {
        validateAvailTextMultiLanguages(availSetting.getListAvailText(), availSetting.getSupportedLanguagesIso());
      }

      boolean isAvailIcon = BooleanUtils.toBoolean(availSetting.getAvailIcon());
      boolean isDropShipmentAvail = BooleanUtils.toBoolean(availSetting.getDropShipmentAvail());
      
      return !(isAvailIcon && isDropShipmentAvail);
    }
    return true;
  }
  
  private void validateAvailTextMultiLanguages(List<SettingLanguageDto> availTexts, List<String> supportedLanguagesIso) {
    if(availTexts.size() != supportedLanguagesIso.size()) {
      throw new IllegalArgumentException(String.format("Missing text setting for supported languages"));
    }
    availTexts.forEach(t -> {
      if(!supportedLanguagesIso.contains(t.getLangIso()) || Objects.isNull(t.getContent())) {
        throw new IllegalArgumentException(String.format("All given languages must to be filled"));
      }
    });

  }

}
