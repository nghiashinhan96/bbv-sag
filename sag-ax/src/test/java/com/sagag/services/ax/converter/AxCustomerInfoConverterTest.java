package com.sagag.services.ax.converter;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.ax.domain.AxCustomer;
import com.sagag.services.ax.translator.AxInvoiceTypeTranslator;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.ax.translator.AxSalesOrderPoolTranslator;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.sag.external.Customer;

@RunWith(MockitoJUnitRunner.class)
public class AxCustomerInfoConverterTest {

  @InjectMocks
  private AxCustomerInfoConverter converter;

  @Mock
  private AxSendMethodTranslator axSendMethodTranslator;

  @Mock
  private AxInvoiceTypeTranslator axInvoiceTypeTranslator;

  @Mock
  private AxSalesOrderPoolTranslator axSalesOrderPoolTranslator;

  @Mock
  private AxPaymentTypeTranslator axPaymentTypeTranslator;

  @Mock
  private CustomerExternalService customerExtService;

  @Test
  public void shouldConvertAxCustomerInfo() {
    Mockito.when(axPaymentTypeTranslator.translateToConnect(Mockito.any()))
        .thenReturn(PaymentMethodType.CARD);
    Mockito.when(axInvoiceTypeTranslator.translateToConnect(Mockito.any()))
        .thenReturn(ErpInvoiceTypeCode.MONTHLY_INVOICE);
    Mockito.when(axSalesOrderPoolTranslator.translateToConnect(Mockito.any()))
        .thenReturn(SupportedAffiliate.MATIK_AT);

    final AxCustomer axCustomerInfo = initAxCustomerInfo();

    final Customer customer = converter.apply(axCustomerInfo);
    Assert.assertThat(customer, Matchers.instanceOf(Customer.class));
    Assert.assertTrue(StringUtils.equalsIgnoreCase(customer.getAffiliateShortName(),
        SupportedAffiliate.MATIK_AT.getAffiliate()));
    Assert.assertTrue(
        StringUtils.equalsIgnoreCase(customer.getCashOrCreditTypeCode(), PaymentMethodType.CARD.name()));
  }

  private AxCustomer initAxCustomerInfo() {
    AxCustomer axCustomer = new AxCustomer();
    axCustomer.setNr("1111101");
    axCustomer.setName("Reini Landtechnik");
    axCustomer.setVatNr("ATU62751356");
    axCustomer.setDefaultBranchId("1024");
    axCustomer.setLanguage("de");
    axCustomer.setBlockedReason("NONE");
    axCustomer.setCurrency("EUR");
    axCustomer.setSendMethod("ABH");
    axCustomer.setPaymentType("Selbstzahl");
    axCustomer.setInvoiceType("WochenFakt");
    axCustomer.setInvoiceTypeDesc("Wochenfaktura");
    axCustomer.setLetterCode("letterCode");
    axCustomer.setSalesOrderPool("01");
    axCustomer.setSalesGroup("D-01024-11");
    axCustomer.setKuKa("465");
    axCustomer.setTermOfPayment("30T");
    axCustomer.setCashDiscount("14T3");
    axCustomer.setDisposalNumber("151627");
    return axCustomer;
  }

}
