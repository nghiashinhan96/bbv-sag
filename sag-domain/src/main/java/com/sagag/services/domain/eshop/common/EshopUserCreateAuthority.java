package com.sagag.services.domain.eshop.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum EshopUserCreateAuthority {

  USER_ADMIN("CUSTOMER_%s_USER_ADMIN", "User admin group of %s"),
  NORMAL_USER("CUSTOMER_%s_NORMAL_USER", "Normal user group of %s"),
  FINAL_USER_ADMIN("FINAL CUSTOMER_%s_USER_ADMIN", "Final user admin group of %s"),
  FINAL_NORMAL_USER("FINAL CUSTOMER_%s_NORMAL_USER", "Final normal user group of %s");

  private String groupNamePattern;

  private String groupDescPattern;

  public String getGroupName(String orgCode) {
    return String.format(this.groupNamePattern, orgCode);
  }

  public String getGroupDesc(String desc) {
    return String.format(this.groupDescPattern, desc);
  }

  public static List<String> names() {
    return Stream.of(EshopUserCreateAuthority.values())
        .map(EshopUserCreateAuthority::name)
        .collect(Collectors.toList());
  }

  public static EshopUserCreateAuthority[] normalValues() {
    return new EshopUserCreateAuthority[]{ USER_ADMIN, NORMAL_USER };
  }

  public static List<String> normalNames() {
    return Stream.of(EshopUserCreateAuthority.frontEndValues(USER_ADMIN.name()))
        .map(EshopUserCreateAuthority::name)
        .collect(Collectors.toList());
  }

  /**
   * When a USER_ADMIN/NORMAL_USER work with Final customer they have to switch to FINAL role
   *
   * @param authority
   * @return final role as String
   */
  public static String switchToFinalRole(EshopUserCreateAuthority authority) {
    switch (authority) {
      case USER_ADMIN:
        return FINAL_USER_ADMIN.name();
      case NORMAL_USER:
        return FINAL_NORMAL_USER.name();
      default:
        return authority.name();
    }
  }

  public static EshopUserCreateAuthority[] frontEndValues(String name) {
    if (isFinalRole(name)) {
      return new EshopUserCreateAuthority[]{FINAL_USER_ADMIN, FINAL_NORMAL_USER};
    }
    return new EshopUserCreateAuthority[]{USER_ADMIN, NORMAL_USER};
  }

  public static EshopUserCreateAuthority normalUser(String name) {
    if (isFinalRole(name)) {
      return FINAL_NORMAL_USER;
    }
    return NORMAL_USER;
  }

  private static boolean isFinalRole(String name) {
    return StringUtils.startsWith(name, "FINAL");
  }

}
