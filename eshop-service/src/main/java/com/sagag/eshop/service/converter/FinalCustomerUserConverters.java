package com.sagag.eshop.service.converter;

import com.sagag.eshop.repo.entity.VUserDetail;
import com.sagag.eshop.service.dto.finalcustomer.FinalCustomerUserDto;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class FinalCustomerUserConverters {

  public static Function<VUserDetail, FinalCustomerUserDto> converter() {
    return col -> FinalCustomerUserDto.builder().userName(col.getUserName()).fullName(col.getFullName())
        .userEmail(col.getUserEmail()).type(col.getRoleName()).userId(col.getUserId())
        .firstName(col.getFirstName()).lastName(col.getLastName()).salutation(col.getSalutCode())
        .build();
  }
}
