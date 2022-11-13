package com.sagag.services.common.contants;

import lombok.experimental.UtilityClass;

/**
 * Constants for user setting class.
 */
@UtilityClass
public final class UserSettingConstants {

  // #3116: for case create new account
  // For every new account the default setting of searching is Tree Search.
  public static final boolean CLASSIC_CATEGORY_VIEW_DEFAULT = false;
  public static final boolean SINGLE_SELECT_MODE_DEFAULT = false;

  public static final int ALLOCATION_ID_DEFAULT = 1;
  public static final int COLLECTIVE_DELIVERY_ID_DEFAULT = 1;
  public static final int DELIVERY_ID_DEFAULT = 1;
  public static final int PAYMENT_METHOD_ID_DEFAULT = 1;
  public static final int INVOICE_TYPE_ID_DEFAULT = 1;

}
