package com.sagag.services.tools.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EshopUserCreateAuthority {

  USER_ADMIN("CUSTOMER_%s_USER_ADMIN", "User admin group of %s"),
  NORMAL_USER("CUSTOMER_%s_NORMAL_USER", "Normal user group of %s");

  private String groupNamePattern;

  private String groupDescPattern;

  public String getGroupName(String orgCode) {
    return String.format(this.groupNamePattern, orgCode);
  }

  public String getGroupDesc(String desc) {
    return String.format(this.groupDescPattern, desc);
  }

}
