package com.sagag.services.gtmotive.domain;

import com.sagag.services.gtmotive.domain.request.AuthenticationData;

import lombok.Data;

@Data
public class GtmotiveProfileDto {

  private String clientid;
  private String userid;
  private String password;
  private String gsId;

  public AuthenticationData toAuthenticationData() {
    return AuthenticationData.builder().gsId(gsId).gsPwd(password).userId(userid)
        .customerId(clientid).build();
  }

}
