package com.sagag.eshop.ci.bamboo.specs.user;

import java.io.Serializable;

import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BambooDeveloperUser implements Serializable {

  private static final long serialVersionUID = 1611607522926712134L;

  @NonNull
  private String username;

  private PermissionType[] permissions;

}
