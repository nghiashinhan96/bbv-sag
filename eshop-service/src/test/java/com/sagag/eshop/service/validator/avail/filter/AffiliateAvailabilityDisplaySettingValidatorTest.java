package com.sagag.eshop.service.validator.avail.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sagag.services.domain.eshop.common.AvailabilityDisplayOption;
import com.sagag.services.domain.eshop.common.AvailabilityDisplayState;
import com.sagag.services.domain.eshop.dto.AffiliateAvailabilityDisplaySettingDto;
import com.sagag.services.domain.eshop.dto.SettingLanguageDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class AffiliateAvailabilityDisplaySettingValidatorTest {

  @InjectMocks
  private AffiliateAvailabilityDisplaySettingValidator validator;

  @Test
  public void validateAvailStateSameDaySuccess() {
    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.SAME_DAY)
        .displayOption(AvailabilityDisplayOption.NONE).title("Same Day").color("Red").build();
    Boolean result = validator.validate(displaySetting);
    assertTrue(result);
  }


  @Test
  public void validateAvailStateSameDayFailInvalidDisplayOption() {
    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.SAME_DAY)
        .displayOption(AvailabilityDisplayOption.DROP_SHIPMENT).title("Same Day").color("Red").build();
    Boolean result = validator.validate(displaySetting);
    assertFalse(result);
  }

  @Test
  public void validateAvailStateNotAvailSuccess() {
    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.NOT_AVAILABLE)
        .displayOption(AvailabilityDisplayOption.DROP_SHIPMENT)
        .confirmColor("Black")
        .title("Not Available").color("Red").build();
    Boolean result = validator.validate(displaySetting);
    assertTrue(result);
  }


  @Test
  public void validateAvailStateNotAvailFailInvalidDisplayOption() {
    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.NOT_AVAILABLE)
        .displayOption(AvailabilityDisplayOption.NONE).title("Not Available").confirmColor("Red")
        .color("Red").build();
    Boolean result = validator.validate(displaySetting);
    assertFalse(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateAvailStateNotAvailFailInvalidDisplayOption2() {
    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.NOT_AVAILABLE)
        .displayOption(AvailabilityDisplayOption.NONE).title("Not Available").color("Red").build();
    validator.validate(displaySetting);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateAvailStateNotAvailFailMissListText() {
    List<String> langSupports = new ArrayList<>();
    langSupports.add("de");
    List<SettingLanguageDto> listAvailTexts = new ArrayList<>();
    listAvailTexts.add(SettingLanguageDto.builder().content("test").langIso("en").build());

    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.NOT_AVAILABLE)
        .displayOption(AvailabilityDisplayOption.NONE).title("Not Available").color("Red")
        .listAvailText(listAvailTexts).supportedLanguagesIso(langSupports).build();
    validator.validate(displaySetting);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateAvailStateNotAvailFailMissListText2() {
    List<String> langSupports = new ArrayList<>();
    langSupports.add("de");
    langSupports.add("en");
    List<SettingLanguageDto> listAvailTexts = new ArrayList<>();
    listAvailTexts.add(SettingLanguageDto.builder().content("test").langIso("en").build());

    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.NOT_AVAILABLE)
        .displayOption(AvailabilityDisplayOption.NONE).title("Not Available").color("Red")
        .listAvailText(listAvailTexts).supportedLanguagesIso(langSupports).build();
    validator.validate(displaySetting);
  }


  @Test(expected = IllegalArgumentException.class)
  public void validateAvailStateNotAvailFailMissDetailText() {
    List<String> langSupports = new ArrayList<>();
    langSupports.add("de");
    List<SettingLanguageDto> detailTexts = new ArrayList<>();
    detailTexts.add(SettingLanguageDto.builder().content("test").langIso("en").build());

    AffiliateAvailabilityDisplaySettingDto displaySetting = AffiliateAvailabilityDisplaySettingDto
        .builder().availState(AvailabilityDisplayState.NOT_AVAILABLE)
        .displayOption(AvailabilityDisplayOption.NONE).title("Not Available").color("Red")
        .detailAvailText(detailTexts).supportedLanguagesIso(langSupports).build();
    validator.validate(displaySetting);
  }

  @Test
  public void validateAvaiStateNotOrderableFailToUpdate() {
    List<String> langSupports = new ArrayList<>();
    langSupports.add("de");
    List<SettingLanguageDto> detailTexts = new ArrayList<>();
    detailTexts.add(SettingLanguageDto.builder().content("test").langIso("de").build());

    List<SettingLanguageDto> listAvailTexts = new ArrayList<>();
    listAvailTexts.add(SettingLanguageDto.builder().content("test").langIso("de").build());

    AffiliateAvailabilityDisplaySettingDto availDisplay = AffiliateAvailabilityDisplaySettingDto.builder()
        .availState(AvailabilityDisplayState.NOT_ORDERABLE)
        .color("color_code")
        .displayOption(AvailabilityDisplayOption.DROP_SHIPMENT)
        .detailAvailText(detailTexts)
        .listAvailText(listAvailTexts)
        .title("Title")
        .supportedLanguagesIso(langSupports)
        .build();
    assertFalse(validator.validate(availDisplay));
  }

  @Test
  public void validateAvaiStateNotOrderableSuccessToUpdate() {
    List<String> langSupports = new ArrayList<>();
    langSupports.add("de");
    List<SettingLanguageDto> detailTexts = new ArrayList<>();
    detailTexts.add(SettingLanguageDto.builder().content("test").langIso("de").build());

    List<SettingLanguageDto> listAvailTexts = new ArrayList<>();
    listAvailTexts.add(SettingLanguageDto.builder().content("test").langIso("de").build());

    AffiliateAvailabilityDisplaySettingDto availDisplay = AffiliateAvailabilityDisplaySettingDto.builder()
        .availState(AvailabilityDisplayState.NOT_ORDERABLE)
        .color("color_code")
        .displayOption(AvailabilityDisplayOption.DISPLAY_TEXT)
        .detailAvailText(detailTexts)
        .listAvailText(listAvailTexts)
        .title("Title")
        .supportedLanguagesIso(langSupports)
        .build();
    assertTrue(validator.validate(availDisplay));
  }

}
