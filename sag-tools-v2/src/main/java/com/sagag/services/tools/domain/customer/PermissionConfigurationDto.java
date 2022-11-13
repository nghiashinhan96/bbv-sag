package com.sagag.services.tools.domain.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionConfigurationDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String permission;

  private String langKey;

  private int permissionId;

  private boolean enable;

  private boolean editable;
}
