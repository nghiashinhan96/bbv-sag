package com.sagag.services.ax.translator;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.services.ax.enums.AxInvoiceTypes;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;

/**
 * Class to verify {@link AxInvoiceTypeTranslator}.
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AtChInvoiceTypeTranslatorTest {

  @InjectMocks
  private AxDefaultInvoiceTypeTranslator axInvoiceTypeTranslator;

  @Mock
  private InvoiceTypeRepository invoiceTypeRepo;

  @Test(expected = UnsupportedOperationException.class)
  public void testTranslateToAxUnsupportedOperationException() {
    axInvoiceTypeTranslator.translateToAx(ErpInvoiceTypeCode.SINGLE_INVOICE);
  }

  @Test(expected = AxCustomerException.class)
  public void testTranslateToConnectWithEmptyInvoiceType() {
    String axInvoiceType = StringUtils.EMPTY;

    axInvoiceTypeTranslator.translateToConnect(axInvoiceType);
  }

  @Test
  public void testTranslateToConnectWithUnsupportedInvoiceType() {
    String axInvoiceType = "NO_SUPPORT_CODE";
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.empty());

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.ALL));
  }

  @Test
  public void testTranslateToConnectWithUnsupportedInvoiceTypeWithNotExistedAtEnum() {
    String axInvoiceType = "NO_SUPPORT_CODE";
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(axInvoiceType));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.ALL));
  }

  @Test
  public void testTranslateToConnectWith_2WFAKT() {
    final String axInvoiceType = AxInvoiceTypes.AX_2WFAKT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE));
  }

  @Test
  public void testTranslateToConnectWith_2WFAKTGT() {
    final String axInvoiceType = AxInvoiceTypes.AX_2WFAKTGT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_EINZELFAGT() {
    final String axInvoiceType = AxInvoiceTypes.AX_EINZELFAGT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.SINGLE_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.SINGLE_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_EINZELFAKT() {
    final String axInvoiceType = AxInvoiceTypes.AX_EINZELFAKT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.SINGLE_INVOICE.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.SINGLE_INVOICE));
  }

  @Test
  public void testTranslateToConnectWith_MONATSFAGT() {
    final String axInvoiceType = AxInvoiceTypes.AX_MONATSFAGT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.MONTHLY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.MONTHLY_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_MONATSFAKT() {
    final String axInvoiceType = AxInvoiceTypes.AX_MONATSFAKT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.MONTHLY_INVOICE.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.MONTHLY_INVOICE));
  }

  @Test
  public void testTranslateToConnectWith_TAGESFAGT() {
    final String axInvoiceType = AxInvoiceTypes.AX_TAGESFAGT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.DAILY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.DAILY_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_TAGESFAKT() {
    final String axInvoiceType = AxInvoiceTypes.AX_TAGESFAKT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.DAILY_INVOICE.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.DAILY_INVOICE));
  }

  @Test
  public void testTranslateToConnectWith_WOCHENFAGT() {
    final String axInvoiceType = AxInvoiceTypes.AX_WOCHENFAGT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.WEEKLY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.WEEKLY_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_WOCHENFAKT() {
    final String axInvoiceType = AxInvoiceTypes.AX_WOCHENFAKT;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.WEEKLY_INVOICE.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(ErpInvoiceTypeCode.WEEKLY_INVOICE));
  }

  @Test
  public void testTranslateToConnectWith_2WFAKTGX() {
    final String axInvoiceType = AxInvoiceTypes.AX_2WFAKTGX;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_MONATSFAGX() {
    final String axInvoiceType = AxInvoiceTypes.AX_MONATSFAGX;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.MONTHLY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.MONTHLY_INVOICE_WITH_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_MONATSFAGW() {
    final String axInvoiceType = AxInvoiceTypes.AX_MONATSFAGW;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.MONTHLY_INVOICE_WEEKLY_CREDIT_SEPARATION));
  }

  @Test
  public void testTranslateToConnectWith_WOCHENFAGX() {
    final String axInvoiceType = AxInvoiceTypes.AX_WOCHENFAGX;
    Mockito.when(invoiceTypeRepo.findInvoiceTypeCodeByInvoiceTypeName(Mockito.eq(axInvoiceType)))
    .thenReturn(Optional.of(ErpInvoiceTypeCode.WEEKLY_INVOICE_WITH_CREDIT_SEPARATION.name()));

    final ErpInvoiceTypeCode invoiceTypeCode =
        axInvoiceTypeTranslator.translateToConnect(axInvoiceType);

    Assert.assertThat(invoiceTypeCode, Matchers.equalTo(
        ErpInvoiceTypeCode.WEEKLY_INVOICE_WITH_CREDIT_SEPARATION));
  }
}
