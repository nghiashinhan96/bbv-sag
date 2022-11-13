package com.sagag.eshop.service.permission.configuration;

import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;

public interface PermissionConfiguration {

  default boolean isPermBelongToAff(String affShortName, String permName) {
    return !(isPermNotBelongToMatikCh(affShortName, permName)
        || isPermNotBelongToRbe(affShortName, permName)
        || isPermNotBelongToDdCh(affShortName, permName)
        || isPermNotBelongToTnm(affShortName, permName)
        || isPermNotBelongToDdAt(affShortName, permName)
        || isPermNotBelongToMatikAt(affShortName, permName)
        || isPermNotBelongToAxCz(affShortName, permName));
  }

  static boolean isPermNotBelongToDdCh(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).isDch()
        && PermissionEnum.valueOfSafely(permName).isWholesaler();
  }

  static boolean isPermNotBelongToTnm(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).isTnm()
        && PermissionEnum.valueOfSafely(permName).isWholesaler();
  }

  static boolean isPermNotBelongToMatikCh(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).isMatikCh()
        && PermissionEnum.valueOfSafely(permName).isWholesaler();
  }

  static boolean isPermNotBelongToRbe(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).isRbe()
        && (PermissionEnum.valueOf(permName).isOffer()
            || PermissionEnum.valueOf(permName).isWholesaler());
  }

  static boolean isPermNotBelongToDdAt(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).isDat()
        && PermissionEnum.valueOfSafely(permName).isWholesaler();
  }

  static boolean isPermNotBelongToMatikAt(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).isMatikAt()
        && PermissionEnum.valueOfSafely(permName).isWholesaler();
  }

  static boolean isPermNotBelongToAxCz(String affShortName, String perName) {
    return SupportedAffiliate.fromDesc(affShortName).isSagCzAffiliate()
        && PermissionEnum.valueOfSafely(perName).isDvse();
  }

  default boolean isValidToAssignFinalCustomerPermission(String permission) {
    return isValidFinalCustomerPermission(permission)
        && !PermissionEnum.HAYNESPRO.name().equals(permission);
  }

  default boolean isValidFinalCustomerPermission(String permission) {
    return !PermissionEnum.WHOLESALER.name().equals(permission)
            && !PermissionEnum.HOME.name().equals(permission)
            && !PermissionEnum.OCI.name().equals(permission)
            && !PermissionEnum.OFFER.name().equals(permission);
  }
}
