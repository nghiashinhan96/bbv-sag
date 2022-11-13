package com.sagag.services.service.coupon.revalidation;

import com.sagag.services.hazelcast.api.CouponCacheService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponValidator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
public abstract class AbstractCouponReValidationStep implements CouponReValidationStep {

  @Autowired
  private CouponCacheService couponCacheService;

  private AbstractCouponReValidationStep nextStep;

  protected abstract String getStepName();

  protected abstract CouponValidator getValidator();

  public AbstractCouponReValidationStep nextStep(AbstractCouponReValidationStep nextStep) {
    this.nextStep = nextStep;
    return this.nextStep;
  }

  @Override
  public boolean shouldHandle(CouponValidationData data) {
    return !data.isEndProcess();
  }

  @Transactional
  public CouponValidationData processRequest(CouponValidationData data) {
    log.debug(getStepName());
    if (shouldHandle(data)) {
      data = handle(data);
    }
    if (!Objects.isNull(nextStep)) {
      return nextStep.processRequest(data);
    }
    return data;
  }

  protected CouponValidationData handleAsDefault(CouponValidationData data) {
    if (getValidator().isInValid(data)) {
      data = handleInvalidCaseAsDefault(data);
    }
    return data;
  }

  protected CouponValidationData handleInvalidCaseAsDefault(CouponValidationData data) {
    data.setEndProcess(true);
    data.setInvalidCoupon(true);
    couponCacheService.clearCache(data.getCoupConditions().getCouponsCode());
    ShoppingCart cart = data.getCart();
    cart.setDiscount(0);
    data.setCart(cart);
    return data;
  }
}
