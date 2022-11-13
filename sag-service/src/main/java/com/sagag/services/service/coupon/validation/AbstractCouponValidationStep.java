package com.sagag.services.service.coupon.validation;

import com.sagag.services.service.coupon.CouponValidationData;
import com.sagag.services.service.coupon.validator.CouponValidator;
import com.sagag.services.service.exception.coupon.CouponValidationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
public abstract class AbstractCouponValidationStep implements CouponValidationStep {

  private AbstractCouponValidationStep nextStep;

  protected abstract String getStepName();

  protected abstract CouponValidator getValidator();

  public AbstractCouponValidationStep nextStep(AbstractCouponValidationStep nextStep) {
    this.nextStep = nextStep;
    return nextStep;
  }

  @Transactional
  public CouponValidationData processRequest(CouponValidationData data)
      throws CouponValidationException {
    log.debug(getStepName());
    data = handle(data);
    if (!Objects.isNull(nextStep)) {
      return nextStep.processRequest(data);
    }
    return data;
  }
}
