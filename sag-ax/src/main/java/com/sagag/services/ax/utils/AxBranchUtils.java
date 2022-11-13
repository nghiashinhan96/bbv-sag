package com.sagag.services.ax.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.common.enums.ErpSendMethodEnum;

import lombok.experimental.UtilityClass;

/**
 * Ax branch Utility.
 *
 */
@UtilityClass
public final class AxBranchUtils {

  public static String getDefaultBranchIfNull(final String branchId) {
    return StringUtils.defaultIfBlank(branchId, AxConstants.DEFAULT_BRANCH_ID);
  }

  public static String defaultBranchId(ErpSendMethodEnum sendMethodEnum, String pickupBranchId,
      String defaultBranchId) {
    return sendMethodEnum == ErpSendMethodEnum.PICKUP ? pickupBranchId : defaultBranchId;
  }

  public static List<String> getBranchIdsByDeliveryType(ErpSendMethodEnum sendMethodEnum,
      String pickupBranchId, List<String> customerBranchIds) {
    return sendMethodEnum == ErpSendMethodEnum.PICKUP ? Arrays.asList(pickupBranchId) : customerBranchIds;
  }
}
