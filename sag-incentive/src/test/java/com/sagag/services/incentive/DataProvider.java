package com.sagag.services.incentive;

import com.sagag.services.common.enums.HashType;
import com.sagag.services.incentive.domain.IncentivePasswordHashDto;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DataProvider {

  public Supplier<String> customerNr(String customer) {
    return () -> customer;
  }

  public Supplier<String> username(String username) {
    return () -> username;
  }

  public Supplier<IncentivePasswordHashDto> passwordHash(String password, HashType type) {
    return () -> {
      IncentivePasswordHashDto dto = new IncentivePasswordHashDto();
      dto.setPassword(password);
      dto.setHashType(type);
      return dto;
    };
  }

}
