package com.sagag.eshop.service.tests.utils;

import static org.junit.Assert.assertThat;

import com.sagag.eshop.repo.entity.InvoiceType;
import com.sagag.eshop.repo.entity.PaymentMethod;
import com.sagag.services.common.utils.DateUtils;

import lombok.experimental.UtilityClass;

import org.hamcrest.Matchers;

import java.util.Date;

@UtilityClass
public class CommonAssertions {

  /**
   * Asserts the testing date value.
   * 
   * @param foundDate the actual date to assert with the expected value.
   */
  public static void assertDate(Date foundDate) {
    assertThat(DateUtils.toStringDate(foundDate, DateUtils.SWISS_DATE_PATTERN),
        Matchers.is(TestsConstants.DATE_VAL_STR));
  }

  /**
   * Asserts the invoice type value.
   * 
   * @param invoiceType the actual invoice type to assert
   */
  public static void assertInvoiceType(InvoiceType invoiceType) {
    assertThat(invoiceType.getId(), Matchers.is(TestsConstants.INVOICETYPE_ID));
    assertThat(invoiceType.getInvoiceTypeCode(), Matchers.is(TestsConstants.TWO_WEEKLY_CREDIT));
    assertThat(invoiceType.getInvoiceTypeName(),
        Matchers.is(TestsConstants.TWO_WEEKLY_CREDIT_TYPE));
    assertThat(invoiceType.getInvoiceTypeDesc(),
        Matchers.is(TestsConstants.TWO_WEEKLY_CREDIT_DESC));
  }

  /**
   * Asserts the payment method value.
   * 
   * @param paymentMethod the actual payment method to assert
   */
  public static void assertPaymentMethod(PaymentMethod paymentMethod) {
    assertThat(paymentMethod.getId(), Matchers.is(TestsConstants.PAYMENT_METHOD_ID));
    assertThat(paymentMethod.getPayMethod(), Matchers.is(TestsConstants.PAY_METHOD_RECHNUNG));
    assertThat(paymentMethod.getDescCode(), Matchers.is(TestsConstants.CREDIT));
    assertThat(paymentMethod.getDescription(), Matchers.is(TestsConstants.PAY_METHOD_RECHNUNG));
    assertThat(paymentMethod.getOrderDisplay(), Matchers.is(1));
  }
}
