package com.sagag.eshop.service.utils;

import com.sagag.services.domain.eshop.common.EshopAuthority;

import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * EshopRole Utility.
 *
 */
@UtilityClass
public final class EshopRoleUtils {

  public static boolean isNormalUserRole(final EshopAuthority role) {
    if (Objects.isNull(role)) {
      return false;
    }
    return EshopAuthority.NORMAL_USER.equals(role);
  }

  public static boolean isUserAdminRole(final EshopAuthority role) {
    if (Objects.isNull(role)) {
      return false;
    }
    return EshopAuthority.USER_ADMIN.equals(role);
  }

  public static boolean isFinalUserRole(final EshopAuthority role) {
    if (Objects.isNull(role)) {
      return false;
    }
    return EshopAuthority.FINAL_NORMAL_USER.equals(role) || EshopAuthority.FINAL_USER_ADMIN.equals(role);
  }
}
