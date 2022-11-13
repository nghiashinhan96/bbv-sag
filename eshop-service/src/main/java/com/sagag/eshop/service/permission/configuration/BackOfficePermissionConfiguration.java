package com.sagag.eshop.service.permission.configuration;

import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;

import java.util.List;

public interface BackOfficePermissionConfiguration<T> extends PermissionConfiguration {

  /**
   * Return permissions.
   *
   * @param setting
   * @return
   */
  List<PermissionConfigurationDto> getPermissions(T setting);

  /**
   * Update permissions
   *
   * @param setting
   */
  void updatePermisions(T setting);
}
