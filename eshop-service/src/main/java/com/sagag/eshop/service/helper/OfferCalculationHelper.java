package com.sagag.eshop.service.helper;

import com.google.common.base.MoreObjects;
import com.sagag.eshop.service.dto.offer.OfferPositionDto;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.offer.OfferActionType;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@UtilityClass
public final class OfferCalculationHelper {

  private static final int SCALE_NUMBER = 2;

  private static final String CHF = "CHF";

  /**
   * Calculates the total gross price.
   *
   * <pre>Refer to EBL source code: Offer.calcTotalLongPrice()</pre>
   *
   * @param positions
   * @return the result of total gross price of list of positions
   */
  public static double calcTotalGrossPrice(final List<OfferPositionDto> positions) {
    if (CollectionUtils.isEmpty(positions)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    BigDecimal amount = BigDecimal.ZERO;
    for (OfferPositionDto position : positions) {
      final Double totalGrossPrice = position != null
          ? MoreObjects.firstNonNull(position.getTotalGrossPrice(), NumberUtils.DOUBLE_ZERO)
          : NumberUtils.DOUBLE_ZERO;
      amount = amount.add(BigDecimal.valueOf(totalGrossPrice.doubleValue()));
    }
    return amount.doubleValue();
  }

  /**
   * Calculates total exclude VAT amount.
   *
   * @param articleAmount
   * @param workAmount
   * @param remarkAmount
   * @return the result as type <code>double</code>
   */
  public static double calcTotalExcludeVatAmount(final double articleAmount,
      final double workAmount, final double remarkAmount) {
    return articleAmount + workAmount + remarkAmount;
  }

  /**
   * Calculates total include VAT amount.
   *
   * @param totalExculdeVatAmount
   * @param vat
   * @return the result as type <code>double</code>
   */
  public static double calcTotalIncludeVatAmount(final double totalExculdeVatAmount,
      final double vat) {
    return BigDecimal.valueOf(totalExculdeVatAmount).multiply(BigDecimal.valueOf(vat))
        .setScale(SCALE_NUMBER, RoundingMode.HALF_EVEN).doubleValue();
  }

  /**
   * Rounds the number
   * <pre>Refer to EBL source code: Offer.round()</pre>
   *
   * @param number
   * @param isoCode
   * @return the result as type <code>BigDecimal</code>
   */
  public static BigDecimal round(final BigDecimal number, final String isoCode) {
    if (StringUtils.isBlank(isoCode) || !StringUtils.equalsIgnoreCase(CHF, isoCode)) {
      return number;
    }
    String k = Float.toString((Math.round(number.floatValue() * 20.0F) / 20.0F));
    k += (k.indexOf('.') == -1) ? ".00" : "00";
    return new BigDecimal(k.substring(0, k.indexOf('.') + 3));
  }

  /**
   * Calculates action amount.
   * <pre>Refer to EBL source code: OfferPosition.calcActionAmount()</pre>
   *
   * @param position the position object
   * @return the result as type <code>BigDecimal</code>
   */
  public static BigDecimal calcActionAmount(final OfferPositionDto position) {
    if (Objects.isNull(position)) {
      return BigDecimal.ZERO;
    }
    final OfferActionType actionType = position.getOfferActionType();
    if (actionType.isNone()) {
      return BigDecimal.ZERO;
    }

    final BigDecimal offerActionValue = BigDecimal.valueOf(position.getActionValue());
    BigDecimal result = BigDecimal.ZERO;
    if (actionType.isPercentType()) {
      final Double totalGrossPrice = position.getTotalGrossPrice();
      if (Objects.nonNull(totalGrossPrice)) {
        result = BigDecimal.valueOf(totalGrossPrice).multiply(offerActionValue)
            .setScale(SCALE_NUMBER, RoundingMode.HALF_EVEN);
      }
      if (actionType.isDiscountMode()) {
        result = result.negate();
      }
    } else if (actionType.isAmountType()) {
      result = offerActionValue;
      if (actionType.isDiscountMode()) {
        result = result.negate();
      }
    }
    return result;
  }

  public static Double round2Digits(Double number) {
    if (number == null) {
      return null;
    }
    return BigDecimal.valueOf(number).setScale(SCALE_NUMBER, RoundingMode.HALF_EVEN).doubleValue();
  }

  public static Double defaultVatToPercentValue(Double vatVal) {
    if (vatVal == null) {
      return vatVal;
    }
    return vatVal * SagConstants.PERCENT;
  }
}
