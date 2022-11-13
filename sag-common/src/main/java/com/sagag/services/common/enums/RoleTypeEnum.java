package com.sagag.services.common.enums;


public enum RoleTypeEnum {
  ADMIN("Administrator role, including sag admin, group admin, user admin"), ASSISTANT(
      "Assistant role, including marketing and sales"), NORMAL_USER(
          "Normal user role, including customer and its employees");

  private final String roleTypeName;

  RoleTypeEnum(String roleName) {
    this.roleTypeName = roleName;
  }

  public String getRoleTypeName() {
    return roleTypeName;
  }

  @Override
  public String toString() {
    return getRoleTypeName();
  }
}
