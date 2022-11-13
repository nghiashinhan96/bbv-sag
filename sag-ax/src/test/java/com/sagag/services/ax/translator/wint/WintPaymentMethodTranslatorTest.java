package com.sagag.services.ax.translator.wint;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.AxPaymentMethod;
import com.sagag.services.ax.translator.PaymentMethodTranslator;
import com.sagag.services.common.enums.PaymentMethodType;

/**
 * Class to verify {@link PaymentMethodTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class WintPaymentMethodTranslatorTest {

  @InjectMocks
  private WintPaymentMethodTranslator axPaymentMethodTranslator;

  @Before
  public void setup() throws Exception {
    axPaymentMethodTranslator.afterPropertiesSet();
  }

  @Test
  public void testTranslateToAxWithCASH() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(
        PaymentMethodType.CASH.name());
    Assert.assertEquals(AxPaymentMethod.CASHENUM.name(), axPaymentMethod);
  }

  @Test
  public void testTranslateToAxWithDIRECTINVOICEENUM() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(
        PaymentMethodType.WHOLESALE.name());
    Assert.assertEquals(AxPaymentMethod.DIRECTINVOICEENUM.name(), axPaymentMethod);
  }

  @Test
  public void testTranslateToAxWithEmpty() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(StringUtils.EMPTY);
    Assert.assertEquals(StringUtils.EMPTY, axPaymentMethod);
  }

  @Test
  public void testTranslateToAxWithStrange() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(
        PaymentMethodType.CARD.name());
    Assert.assertEquals(StringUtils.EMPTY, axPaymentMethod);
  }
}
