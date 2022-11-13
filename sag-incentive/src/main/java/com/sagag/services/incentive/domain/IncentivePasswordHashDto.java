package com.sagag.services.incentive.domain;

import com.sagag.services.common.enums.HashType;

import lombok.Data;

@Data
public class IncentivePasswordHashDto {

  private String password;

  private HashType hashType;

}
