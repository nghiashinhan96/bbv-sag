package com.sagag.services.domain.eshop.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.function.Predicate;

public enum EshopAuthority {

  SYSTEM_ADMIN,
  GROUP_ADMIN,
  USER_ADMIN,
  SALES_ASSISTANT,
  MARKETING_ASSISTANT,
  NORMAL_USER,
  FINAL_USER_ADMIN,
  FINAL_NORMAL_USER,
  AUTONET_USER_ADMIN;

  public static boolean isFinalUserAuthority(Collection<String> authorities) {
    if (CollectionUtils.isEmpty(authorities)) {
      return false;
    }
    return authorities.stream().anyMatch(finalUserPredicate());
  }

  private static Predicate<String> finalUserPredicate() {
    return auth -> StringUtils.equals(FINAL_USER_ADMIN.name(), StringUtils.defaultString(auth))
        || StringUtils.equals(FINAL_NORMAL_USER.name(), StringUtils.defaultString(auth));
  }

  public static boolean isCustomerUserAdmin(String name) {
    EshopAuthority eval = EnumUtils.getEnum(EshopAuthority.class, name);
    return USER_ADMIN == eval || FINAL_USER_ADMIN == eval;
  }

  public static boolean isSaleAssistant(String name) {
    EshopAuthority eval = EnumUtils.getEnum(EshopAuthority.class, name);
    return SALES_ASSISTANT == eval;
  }
}
