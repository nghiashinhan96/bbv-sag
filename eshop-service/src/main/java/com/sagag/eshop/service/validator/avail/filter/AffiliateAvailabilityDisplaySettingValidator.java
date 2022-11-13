package com.sagag.eshop.service.validator.avail.filter;

import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.SettingLanguageDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Component
public class AffiliateAvailabilityDisplaySettingValidator
    implements IDataValidator<AffiliateAvailabilityDisplaySettingDto> {

  @Override
  public boolean validate(AffiliateAvailabilityDisplaySettingDto availDisplaySetting) {
    if (!Objects.isNull(availDisplaySetting)) {
      Assert.notNull(availDisplaySetting.getAvailState(), "State must not be null");
      Assert.notNull(availDisplaySetting.getDisplayOption(), "Display option must not be null");

      Assert.hasText(availDisplaySetting.getTitle(), "Title must not be blank");
      Assert.hasText(availDisplaySetting.getColor(), "Color must not be blank");

      if (CollectionUtils.isNotEmpty(availDisplaySetting.getDetailAvailText())) {
        validateAvailTextMultiLanguages(availDisplaySetting.getDetailAvailText(),
            availDisplaySetting.getSupportedLanguagesIso());
      }
      if (CollectionUtils.isNotEmpty(availDisplaySetting.getListAvailText())) {
        validateAvailTextMultiLanguages(availDisplaySetting.getListAvailText(),
            availDisplaySetting.getSupportedLanguagesIso());
      }

      //In case not available state use selected options
      if (availDisplaySetting.getAvailState().isNotAvailable()) {
        return validateNotAvailableState(availDisplaySetting);
      }

      if(availDisplaySetting.getAvailState().isNotOrderableState()) {
        return availDisplaySetting.getDisplayOption().isDisplayTextOptionOnly();
      }

      //Other states use display text option
      return availDisplaySetting.getDisplayOption().isDisplayTextOption();
    }
    return true;
  }

  private boolean validateNotAvailableState(
      AffiliateAvailabilityDisplaySettingDto availDisplaySetting) {
    Assert.hasText(availDisplaySetting.getConfirmColor(),
        "'To be confirmed' color not be blank for Not Available state");
    return availDisplaySetting.getDisplayOption().isSelectedOption();
  }

  private void validateAvailTextMultiLanguages(List<SettingLanguageDto> availTexts,
      List<String> supportedLanguagesIso) {
    if (availTexts.size() != supportedLanguagesIso.size()) {
      throw new IllegalArgumentException(
          String.format("Missing text setting for supported languages"));
    }
    availTexts.forEach(t -> {
      if (!supportedLanguagesIso.stream()
          .anyMatch(lang -> StringUtils.equalsIgnoreCase(lang, t.getLangIso()))
          || Objects.isNull(t.getContent())) {
        throw new IllegalArgumentException(String.format("All given languages must to be filled"));
      }
    });
  }

}
