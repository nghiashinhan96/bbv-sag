package com.sagag.services.rest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.services.domain.eshop.dto.SecurityCodeRequestDto;

import lombok.experimental.UtilityClass;

/**
 * Data test for rest module.
 */
@UtilityClass
public final class DataTests {

  public static String getMockedJsonOfEshopUserDto(final String username, final String affiliateId,
      final String langCode) throws JsonProcessingException {

    SecurityCodeRequestDto dto = new SecurityCodeRequestDto();
    dto.setLangCode(langCode);
    dto.setAffiliateId(affiliateId);
    dto.setUsername(username);
    dto.setRedirectUrl("https://www.d-store.ch/");

    return new ObjectMapper().writeValueAsString(dto);
  }
}
