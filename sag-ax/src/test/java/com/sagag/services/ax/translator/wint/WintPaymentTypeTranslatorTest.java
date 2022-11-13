package com.sagag.services.ax.translator.wint;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.wint.WtPaymentType;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.common.enums.PaymentMethodType;

/**
 * Class to verify {@link AxPaymentTypeTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WintPaymentTypeTranslatorTest {

  @InjectMocks
  private WintPaymentTypeTranslator axPaymentTypeTranslator;

  @Test
  public void givenEmptyPaymentTypeShouldReturnCash() {
    Assert.assertThat(axPaymentTypeTranslator.translateToConnect(StringUtils.EMPTY),
        Matchers.is(PaymentMethodType.CASH));
  }

  @Test
  public void givenCashPaymentTypeShouldReturnCash() {
    Assert.assertThat(axPaymentTypeTranslator.translateToConnect(WtPaymentType.CASH.getCode()),
        Matchers.is(PaymentMethodType.CASH));
  }

  @Test
  public void givenWholesalePaymentTypeShouldReturnWholeSale() {
    Assert.assertThat(axPaymentTypeTranslator.translateToConnect(WtPaymentType.WHOLESALE.getCode()),
        Matchers.is(PaymentMethodType.WHOLESALE));
  }

  @Test(expected = AxCustomerException.class)
  public void givenStrangePaymentTypeShouldThrowException() {
    axPaymentTypeTranslator.translateToConnect("STRANGE");
  }

}
