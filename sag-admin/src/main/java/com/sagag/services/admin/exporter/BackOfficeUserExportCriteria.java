package com.sagag.services.admin.exporter;

import com.sagag.services.domain.eshop.backoffice.dto.ExportingUserDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Back office user export criteria.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BackOfficeUserExportCriteria {

  private final List<ExportingUserDto> users;

}
