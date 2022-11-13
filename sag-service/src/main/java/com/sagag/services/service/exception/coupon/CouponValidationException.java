package com.sagag.services.service.exception.coupon;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The generic exception class for Coupon validation exception.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CouponValidationException extends ValidationException {

  private static final long serialVersionUID = -4892470080331626695L;

  /** Error message key for invalid coupon for affiliate. */
  private static final String COUPON_ERROR_INVALID_FOR_AFFI = "COUPON.ERROR.INVALID_FOR_AFFI";

  /** Error message key for invalid coupon for customer. */
  private static final String COUPON_ERROR_INVALID_FOR_SHOP = "COUPON.ERROR.INVALID_FOR_SHOP";

  private String couponCode;

  public CouponValidationException(String message) {
    super(message);
  }

  /**
   * Error business code enumeration for specific cases when coupon validation.
   */
  public enum CouponErrorCase implements IBusinessCode {

    CE_EXP_001("COUPON.ERROR.EXPIRED"), // coupon expired
    CE_FUS_001("COUPON.ERROR.FULLY_USED"), // coupon fully used
    CE_IAF_001(COUPON_ERROR_INVALID_FOR_AFFI), // gen_art id is matched
    CE_IAF_002(COUPON_ERROR_INVALID_FOR_AFFI), // no brand id is matched
    CE_IAF_003(COUPON_ERROR_INVALID_FOR_AFFI), // no pim_id is matched
    CE_IAF_004(COUPON_ERROR_INVALID_FOR_AFFI), // price is greater or equals than zero
    CE_ICU_001(COUPON_ERROR_INVALID_FOR_SHOP), // not applied for any affiliate
    CE_ICU_002(COUPON_ERROR_INVALID_FOR_SHOP), // is not applied for customer
    CE_LUS_001("COUPON.ERROR.LIMIT_USED"), // coupon limit used
    CE_MAT_001("COUPON.ERROR.COUPON_LIMIT"), // total quantiy is greater than maximum amount
    CE_MIT_001("COUPON.ERROR.TOTAL_MINIMUM"), // total quantity is less than minimum amount
    CE_NFO_001("COUPON.ERROR.NOT_FOUND"); // not found coupon

    private String key;

    CouponErrorCase(String key) {
      this.key = key;
    }

    /**
     * Returns code for the error case.
     * 
     * @return the unique code for the case
     */
    @Override
    public String code() {
      return this.name();
    }

    /**
     * Returns multilingal message key for the error case.
     * 
     * @return the multilingal message key
     */
    @Override
    public String key() {
      return this.key;
    }
  }

}
