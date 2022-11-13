package com.sagag.eshop.service.utils;

import com.sagag.eshop.service.dto.FunctionDto;
import com.sagag.eshop.service.dto.PermissionDto;
import com.sagag.services.common.enums.PermissionEnum;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@UtilityClass
public class PermissionUtils {

  public static final String TYPE_LINK_SHOP = "shop";

  private static final List<String> FUNCTIONS_ACCESS_SHOPS =
      Arrays.asList("OIL_URL_ACCESS", "BULB_URL_ACCESS", "BATTERY_URL_ACCESS", "TYRE_URL_ACCESS");

  public boolean hasUrlPermission(List<FunctionDto> functions, String url) {
    if (CollectionUtils.isEmpty(functions) || StringUtils.isBlank(url)) {
      return false;
    }
    return functions.stream().anyMatch(p -> {
      String pattern = "^".concat(p.getRelativeUrl()).concat("$").replace("**", ".*?");
      return url.toLowerCase().matches(pattern);
    });
  }

  public List<FunctionDto> getFunctionGrantedList(List<PermissionDto> perms,
      List<String> functionNames) {
    if (CollectionUtils.isEmpty(perms) || CollectionUtils.isEmpty(functionNames)) {
      return Collections.emptyList();
    }
    List<FunctionDto> functions = new LinkedList<>();
    perms.stream()
        .forEach(perm -> perm.getFunctions().stream()
            .filter(func -> functionNames.indexOf(func.getFunctionName()) != -1)
            .forEach(functions::add));

    return functions;
  }

  public boolean isActiveLink(String externalType, String attribute,
      Map<String, Boolean> shopAccessPermission) {
    if (StringUtils.equals(externalType, TYPE_LINK_SHOP) && !StringUtils.isEmpty(attribute)) {
      return isAccess(shopAccessPermission, attribute.toUpperCase());
    }
    return true;
  }

  public boolean isAccess(Map<String, Boolean> shopAccessPermission, String shopName) {
    return shopAccessPermission.getOrDefault(shopName, false);
  }

  public Map<String, Boolean> getPermissionShopUniparts(List<PermissionDto> perms) {
    Map<String, Boolean> shopAccessMap = new HashMap<>();
    shopAccessMap.put(PermissionEnum.OIL.name(), false);
    shopAccessMap.put(PermissionEnum.BULB.name(), false);
    shopAccessMap.put(PermissionEnum.BATTERY.name(), false);
    shopAccessMap.put(PermissionEnum.TYRE.name(), false);

    List<FunctionDto> functions =
        PermissionUtils.getFunctionGrantedList(perms, FUNCTIONS_ACCESS_SHOPS);

    shopAccessMap.forEach((shopName, access) -> {
      boolean isAccess =
          functions.stream().anyMatch(func -> func.getFunctionName().contains(shopName));
      shopAccessMap.put(shopName, isAccess);
    });

    return shopAccessMap;
  }

}
