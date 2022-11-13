package com.sagag.services.service.user.permission;

import com.sagag.eshop.service.dto.FunctionDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PermissionEnum;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class DefaultPermissionBuilder {

  public List<PermissionDto> buildDefaultPermissions(UserInfo user) {
    List<PermissionDto> permissions = new ArrayList<>();

    if (!user.isFinalUserRole()) {
      permissions.add(buildShowPromotionPermission());
    }

    if (user.isSaleOnBehalf() || user.isUserAdminRole()
        || (user.isCustomerRole() && user.getSettings().isViewBilling())) {
      permissions.add(buildInvoiceHistoryPermission());
    }
    return permissions;
  }

  private static PermissionDto buildInvoiceHistoryPermission() {
    final PermissionDto permission = buildPermission("INVOICE_HISTORY");
    permission
        .setFunctions(Arrays.asList(buildFunction("USED_INVOICE_HISTORY", StringUtils.EMPTY)));
    return permission;
  }

  private static PermissionDto buildShowPromotionPermission() {
    final PermissionDto permission = buildPermission("SHOW_PROMOTION");
    permission.setFunctions(Arrays.asList(buildFunction("USED_SHOW_PROMOTION", StringUtils.EMPTY)));
    return permission;
  }

  public static PermissionDto buildPermission(String permission) {
    return PermissionDto.builder().permission(permission).build();
  }

  public static FunctionDto buildFunction(String functionName, String relativeUrl) {
    return FunctionDto.builder().functionName(functionName).relativeUrl(relativeUrl).build();
  }

  public static void addVinPackagesFunctionToUserInfo(UserInfo user) {
    if (user.isFinalUserRole()) {
      return;
    }
    user.getPermissions().stream().filter(p -> PermissionEnum.VIN.name().equals(p.getPermission()))
        .findFirst().ifPresent(p -> p.getFunctions().add(buildVinPackagesFunction()));
  }

  private static FunctionDto buildVinPackagesFunction() {
    return buildFunction("VIN_API_PACKAGES", "/vin/packages**");
  }

}
