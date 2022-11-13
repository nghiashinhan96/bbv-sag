package com.sagag.eshop.service.api;

import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateInfoDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateSettingDto;
import com.sagag.services.domain.eshop.dto.BackOfficeAffiliateShortInfoDto;
import com.sagag.services.domain.eshop.dto.OrganisationDto;

import java.util.List;

public interface AffiliateService {

  List<OrganisationDto> getAllAffiliates();

  /**
   * Returns affiliate short information.
   *
   * @return affiliate short information
   */
  List<BackOfficeAffiliateShortInfoDto> getShortInfos();

  /**
   * Returns affiliate information.
   *
   * @param affShortName
   * @return affiliate information
   */
  List<BackOfficeAffiliateInfoDto> getInfos(String affShortName);

  /**
   * Returns affiliate settings.
   *
   * @param affShortName
   * @return affiliate setting
   */
  BackOfficeAffiliateSettingDto getSettings(String affShortName);


  /**
   * Returns affiliate short information by country.
   * @param countryShortCode 
   *
   * @return affiliate short information by country
   */
  List<BackOfficeAffiliateShortInfoDto> getShortInfosByCountry(String countryShortCode);
}
