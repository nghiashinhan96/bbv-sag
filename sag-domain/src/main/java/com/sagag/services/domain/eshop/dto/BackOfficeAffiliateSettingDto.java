package com.sagag.services.domain.eshop.dto;

import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class BackOfficeAffiliateSettingDto extends BackOfficeAffiliateInfoDto {

  private static final long serialVersionUID = 1L;

  private List<PermissionConfigurationDto> perms;
}
