package com.sagag.services.gtmotive.dto.request.gtinterface;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;

import lombok.Data;
import lombok.NonNull;

@Data
public class GtmotivePartUpdateCriteria {

  @NonNull
  private AuthenticationData authenData;

  @NonNull
  private GtmotivePartUpdateRequest request;
}
