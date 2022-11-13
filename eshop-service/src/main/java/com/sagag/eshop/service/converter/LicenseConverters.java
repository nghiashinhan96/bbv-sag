package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.License;
import com.sagag.eshop.repo.entity.LicenseSettings;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class LicenseConverters {

  public static Function<LicenseSettings, LicenseSettingsDto> licenseSettingsConverter() {
    return licenseSetting -> SagBeanUtils.map(licenseSetting, LicenseSettingsDto.class);
  }

  public static Function<LicenseDto, License> licenseEntityConverter() {
    return licenseDto -> SagBeanUtils.map(licenseDto, License.class);
  }

  public static Function<License, BackOfficeLicenseDto> licenseBackOfficeConverter() {
    return item -> {
      BackOfficeLicenseDto backOfficeLicenseDto = SagBeanUtils.map(item, BackOfficeLicenseDto.class);
      backOfficeLicenseDto.setTypeOfLicense(backOfficeLicenseDto.getTypeOfLicense().trim());
      backOfficeLicenseDto.setPackName(backOfficeLicenseDto.getPackName().trim());
      backOfficeLicenseDto.setBeginDate(DateUtils.toStringDate(item.getBeginDate(), DateUtils.UTC_DATE_PATTERN));
      backOfficeLicenseDto.setEndDate(DateUtils.toStringDate(item.getEndDate(), DateUtils.UTC_DATE_PATTERN));
      return backOfficeLicenseDto;
    };
  }
}
