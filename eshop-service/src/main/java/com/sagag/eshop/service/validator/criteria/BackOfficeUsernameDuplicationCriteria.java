package com.sagag.eshop.service.validator.criteria;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.services.domain.eshop.dto.BackOfficeUserSettingDto;

import lombok.Data;

@Data
public class BackOfficeUsernameDuplicationCriteria {

  private final BackOfficeUserSettingDto backOfficeUserSettingDto;
  private final EshopUser eshopUser;
}
