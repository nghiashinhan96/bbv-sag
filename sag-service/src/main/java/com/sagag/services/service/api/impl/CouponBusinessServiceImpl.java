package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.entity.CouponConditions;
import com.sagag.eshop.repo.entity.CouponUseLog;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.api.CouponBusinessService;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.revalidation.CouponReValidationProcess;
import com.sagag.services.service.coupon.validation.CouponValidationProcess;
import com.sagag.services.service.exception.coupon.CouponValidationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation class for Coupon business service.
 *
 */
@Service
@Slf4j
public class CouponBusinessServiceImpl implements CouponBusinessService {

  private static final String DILIMITER = ",";

  @Autowired
  private CouponCacheService couponCacheService;

  @Autowired
  private CouponValidationProcess couponValidationProcess;

  @Autowired
  private CouponReValidationProcess couponReValidationProcess;

  @Override
  public ShoppingCart checkCouponsAvailable(String couponsCode, UserInfo user, ShoppingCart cart)
      throws CouponValidationException {
    CouponValidationData data = CouponValidationData.builder().couponCode(couponsCode).user(user)
        .cart(cart).appliedItems(cart.getItems()).build();
    CouponValidationData results = couponValidationProcess.getFirstStep().processRequest(data);
    return extractResult(results);
  }

  @Override
  public void validateCoupon(UserInfo user, ShoppingCart shoppingCart) {
    final CouponUseLog couponLog = couponCacheService.getCouponUseLog(user.key());
    if (!couponLog.hasCouponCode()) {
      return;
    }
    final long start = System.currentTimeMillis();
    CouponValidationData data =
        CouponValidationData.builder().couponCode(couponLog.getCouponsCode()).user(user)
            .cart(shoppingCart).appliedItems(shoppingCart.getItems()).build();
    CouponValidationData results = couponReValidationProcess.getFirstStep().processRequest(data);
    if (!results.isInvalidCoupon()) {
      extractResult(results);
    }
    log.debug("CouponBusinessServiceImpl->validateCoupon->validate Coupon {} ms",
        System.currentTimeMillis() - start);
  }

  private ShoppingCart extractResult(CouponValidationData results) {
    final UserInfo user = results.getUser();
    final String couponsCode = results.getCouponCode();
    final double discountNetPrice = negateDiscount(results.getDiscountNetPrice());
    final CouponConditions coupConditions = results.getCoupConditions();
    final List<ShoppingCartItem> itemAppliedItems = results.getAppliedItems();
    final long discountArticleId = coupConditions.getDiscountArticleId();
    final ShoppingCart shoppingCart = results.getCart();
    final int remainUsage = results.getRemainUsage();
    final CouponUseLog couponItem =
        getCouponItemLog(couponsCode, discountNetPrice, discountArticleId, itemAppliedItems, user,
            remainUsage, coupConditions.getUmarId(), coupConditions.getCountry());

    couponCacheService.add(couponItem, user.key());
    shoppingCart.setCouponCode(couponsCode);
    shoppingCart.setDiscount(discountNetPrice);
    return shoppingCart;
  }

  // Note: from #3688
  private double negateDiscount(double discountNetPrice) {
    return -discountNetPrice;
  }

  private CouponUseLog getCouponItemLog(String couponsCode, double amount, long discountArticleID,
      List<ShoppingCartItem> cartItems, UserInfo user, int remain, long umarId,
      String countryMatch) {
    final CouponUseLog couponItem = new CouponUseLog();
    couponItem.setUserId(user.getId().toString());
    couponItem.setAffiliateMatch(user.getAffiliateShortName());
    couponItem.setCouponsCode(couponsCode);
    couponItem.setCountryMatch(countryMatch);
    couponItem.setAmoutApplied(amount);
    couponItem.setCustomerNr(user.getCustNrStr());
    final String idSagsys = cartItems.stream().map(item -> item.getArticleItem().getIdSagsys())
        .collect(Collectors.joining(DILIMITER));
    final String brand = cartItems.stream().map(item -> item.getArticleItem().getIdProductBrand())
        .collect(Collectors.joining(DILIMITER));
    final String genartID = cartItems.stream().map(item -> item.getArticleItem().getGaId())
        .collect(Collectors.joining(DILIMITER));
    couponItem.setBrandsMatch(brand);
    couponItem.setArticleCategories(genartID);
    couponItem.setArticleIdMatch(idSagsys);
    couponItem.setDateUsed(Calendar.getInstance().getTime());
    couponItem.setUsageCountRemainder(remain);
    couponItem.setDiscountArticleId(discountArticleID);
    couponItem.setUmarId(umarId);
    return couponItem;
  }

  @Override
  public ShoppingCart removeExistingCoupon(final UserInfo user, final ShoppingCart cart) {
    couponCacheService.clearCache(user.key());
    cart.setDiscount(0);
    return cart;
  }
}
