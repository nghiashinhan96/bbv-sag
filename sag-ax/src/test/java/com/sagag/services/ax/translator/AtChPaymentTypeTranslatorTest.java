package com.sagag.services.ax.translator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.ax.enums.AxPaymentType;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.PaymentMethodType;

/**
 * Class to verify {@link AxPaymentTypeTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AtChPaymentTypeTranslatorTest {

  private static final String NO_SUPPORT_TXT = "NO_SUPPORT";

  @InjectMocks
  private AxDefaultPaymentTypeTranslator axPaymentTypeTranslator;

  @InjectMocks
  private AxPaymentTypeForSalesTranslator axPaymentTypeForSalesTranslator;

  @InjectMocks
  private AxPaymentTypeForSalesWithKSLTranslator axPaymentTypeForSalesWithKSLTranslator;

  @Test(expected = AxCustomerException.class)
  public void testMapWithEmptyString() {
    axPaymentTypeTranslator.translateToConnect(StringUtils.EMPTY);
  }

  @Test(expected = AxCustomerException.class)
  public void testMapWithNoSupportPaymentType() {
    axPaymentTypeTranslator.translateToConnect(NO_SUPPORT_TXT);
  }

  @Test
  public void testMapWithBar() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.BAR.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithBetreibung() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.BETREIBUNG.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithKarte() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.KARTE.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithSak() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SAK.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithSepa_B_Sak() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_B_SAK.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithSepa_C_Sak() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_C_SAK.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithSofort() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SOFORT.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, cashOrCreditType);
  }

  @Test
  public void testMapWithFin() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test
  public void testMapWithKeine() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.KEINE.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test
  public void testMapWithSelbstzahl() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SELBSTZAHL.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test
  public void testMapWithSepa_B_Fin() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_B_FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test
  public void testMapWithSepa_B2B() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_B2B.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test
  public void testMapWithSepa_C_Fin() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_C_FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test
  public void testMapWithSepa_Core() {
    final PaymentMethodType cashOrCreditType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_CORE.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, cashOrCreditType);
  }

  @Test(expected = AxCustomerException.class)
  public void testTranslateToConnectForSales_Empty_WithoutKSL() {
    axPaymentTypeForSalesTranslator.translateToConnect(StringUtils.EMPTY);
  }

  @Test(expected = AxCustomerException.class)
  public void testTranslateToConnectForSales_NoSupport_WithoutKSL() {
    axPaymentTypeForSalesTranslator.translateToConnect(NO_SUPPORT_TXT);
  }

  @Test
  public void testTranslateToConnectForSales_Fin_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Keine_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.KEINE.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Selbstzahl_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SELBSTZAHL.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaBFin_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SEPA_B_FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaB2B_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SEPA_B2B.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaCFin_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeTranslator.translateToConnect(AxPaymentType.SEPA_C_FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaCore_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SEPA_CORE.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Bar_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.BAR.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Bar_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.BAR.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Betreibung_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.BETREIBUNG.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Betreibung_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.BETREIBUNG.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Karte_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.KARTE.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Karte_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.KARTE.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Sak_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.SAK.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Sak_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SAK.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaBSak_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.SEPA_B_SAK.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaBSak_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SEPA_B_SAK.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaCSak_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.SEPA_C_SAK.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_SepaCSak_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SEPA_C_SAK.getCode());
    Assert.assertEquals(PaymentMethodType.CASH, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Sofort_WithoutKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesTranslator.translateToConnect(AxPaymentType.SOFORT.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Sofort_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.SOFORT.getCode());
    Assert.assertEquals(PaymentMethodType.DIRECT_INVOICE, connectPaymentType);
  }

  @Test
  public void testTranslateToConnectForSales_Fin_WithKSL() {
    final PaymentMethodType connectPaymentType = axPaymentTypeForSalesWithKSLTranslator.translateToConnect(AxPaymentType.FIN.getCode());
    Assert.assertEquals(PaymentMethodType.CREDIT, connectPaymentType);
  }

  @Test(expected = AxCustomerException.class)
  public void testTranslateToConnectForSales_InvalidType_WithKSL() {
    axPaymentTypeForSalesWithKSLTranslator.translateToConnect("Invalid type");
  }

  @Test(expected = AxCustomerException.class)
  public void testTranslateToConnectForSales_Empty_WithKSL() {
    axPaymentTypeForSalesWithKSLTranslator.translateToConnect(StringUtils.EMPTY);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTranslateToAX_WithKSL() {
    axPaymentTypeForSalesWithKSLTranslator.translateToAx(PaymentMethodType.CREDIT);
  }
}
