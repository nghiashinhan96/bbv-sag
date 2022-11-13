package com.sagag.services.gtmotive.dto.request.mainmoduleservice;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GtmotiveEquipmentOptionsSearchCriteria {

  @NonNull
  private AuthenticationData authenData;

  @NonNull
  private GtmotiveEquipmentOptionsSearchRequest searchRequest;
}
