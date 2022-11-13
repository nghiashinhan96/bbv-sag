package com.sagag.services.ax.translator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.AxPaymentMethod;
import com.sagag.services.common.enums.PaymentMethodType;

/**
 * Class to verify {@link PaymentMethodTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AtChPaymentMethodTranslatorTest {

  @InjectMocks
  private AxDefaultPaymentMethodTranslator axPaymentMethodTranslator;

  @Before
  public void setup() throws Exception {
    axPaymentMethodTranslator.afterPropertiesSet();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTranslateToConnectUnsupport() {
    axPaymentMethodTranslator.translateToConnect(PaymentMethodType.CARD.name());
  }

  @Test
  public void testTranslateToAxWithCASH() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(PaymentMethodType.CASH.name());
    Assert.assertEquals(AxPaymentMethod.CASH.name(), axPaymentMethod);
  }

  @Test
  public void testTranslateToAxWithCREDIT() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(PaymentMethodType.CREDIT.name());
    Assert.assertEquals(AxPaymentMethod.RECHNUNG.name(), axPaymentMethod);
  }

  @Test
  public void testTranslateToAxWithDIRECTINVOICE() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(PaymentMethodType.DIRECT_INVOICE.name());
    Assert.assertEquals(AxPaymentMethod.DIRECTINVOICE.name(), axPaymentMethod);
  }

  @Test
  public void testTranslateToAxWithCARD() {
    final String axPaymentMethod = axPaymentMethodTranslator.translateToAx(PaymentMethodType.CARD.name());
    Assert.assertEquals(AxPaymentMethod.CARD.name(), axPaymentMethod);
  }

}
