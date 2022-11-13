package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BackOfficeUserProfileDto {
  private String affiliateShortName;
  private Long customerNumber;
  private String addressesLink;
  private String sendMethodCode;
  private UserProfileDto userProfileDto;
}
