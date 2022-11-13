package com.sagag.services.service.order.validator.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.utils.SagValidationUtils;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.order.OrderRequestTypeValidatorFactory;
import com.sagag.services.service.order.model.OrderRequestType;
import com.sagag.services.service.order.model.OrderValidateCriteria;
import com.sagag.services.service.order.validator.OrderRequestTypeValidator;
import com.sagag.services.service.order.validator.OrderValidator;
import com.sagag.services.service.request.order.CreateOrderRequestBodyV2;
import com.sagag.services.service.request.order.OrderConditionContextBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import javax.validation.ConstraintViolation;

@Component
public class OrderValidatorImpl implements OrderValidator {

  private static final boolean IGNORE_PAYMENT_VALIDATION_FOR_CZ_COUNTRY = true;

  @Autowired
  private OrderRequestTypeValidatorFactory orderRequestTypeValidatorFactory;

  @Override
  public boolean validate(OrderValidateCriteria criteria) throws ValidationException {
    final CreateOrderRequestBodyV2 body = criteria.getBody();
    final UserInfo user = criteria.getUser();
    final OrderRequestType requestType = criteria.getRequestType();

    // Validate customer is active or otherwise
    final Customer customer = user.getCustomer();
    if (!customer.isActive()) {
      throw AxCustomerException.buildBlockedCustomerException(customer.getBlockReason());
    }

    // Validate order request
    final Optional<ConstraintViolation<CreateOrderRequestBodyV2>> orderRequestViolation =
        SagValidationUtils.validateObjectAndReturnFirstError(body);
    if (orderRequestViolation.isPresent()) {
      throw new IllegalArgumentException(orderRequestViolation.get().getMessage());
    }

    final OrderConditionContextBody orderCondition = criteria.getOrderCondition();
    final Optional<ConstraintViolation<OrderConditionContextBody>> orderConditionViolation =
        SagValidationUtils.validateObjectAndReturnFirstError(orderCondition);
    if (orderConditionViolation.isPresent()) {
      throw new IllegalArgumentException(orderConditionViolation.get().getMessage());
    }

    // #4240: [STG-CZ]: AX9 Orders- Payment Type handling
    // Ignoring the validation rule for CZ
    if (user.getSupportedAffiliate().isCzAffiliate()) {
      return IGNORE_PAYMENT_VALIDATION_FOR_CZ_COUNTRY;
    }

    // Validate payment method info, if sales login on behalf, its passed
    // the payment method validation
    final PaymentMethodType paymentMethod = orderCondition.getPaymentMethodType();
    if (!user.isSaleOnBehalf() && paymentMethod.justAllowForSales()) {
      throw new IllegalArgumentException("The given pjustAllowForSalesayment method is not allow for B2B user");
    }

    final OrderRequestTypeValidator orderRequestTypeValidation =
        orderRequestTypeValidatorFactory.getOrderRequestTypeValidation(user.isSaleOnBehalf());
    if (!orderRequestTypeValidation.validate(orderCondition, requestType)) {
      throw new IllegalArgumentException(
          "The given payment method or delivery type is not allow for ABS order");
    }
    return true;
  }

}
