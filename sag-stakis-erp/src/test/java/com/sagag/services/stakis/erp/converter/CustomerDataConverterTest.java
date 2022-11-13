package com.sagag.services.stakis.erp.converter;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.converter.impl.customer.CisCustomerDataConverterImpl;
import com.sagag.services.stakis.erp.translator.CisContactTranslator;
import com.sagag.services.stakis.erp.translator.CisCustomerBranchTranslator;
import com.sagag.services.stakis.erp.translator.CisDeliveryInfoTranslator;
import com.sagag.services.stakis.erp.translator.CisInvoiceTypeTranslator;
import com.sagag.services.stakis.erp.translator.CisPaymentDataTranslator;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CustomerDataConverterTest {

  @InjectMocks
  private CisCustomerDataConverterImpl converter;

  @Mock
  private CisPaymentDataTranslator paymentDataTranslator;

  @Mock
  private CisInvoiceTypeTranslator invoiceTypeTranslator;

  @Mock
  private CisDeliveryInfoTranslator deliveryInfoTranslator;

  @Mock
  private CisCustomerBranchTranslator custBranchTranslator;

  @Mock
  private CisContactTranslator contactTranslator;

  private OutCustomer outCustomer;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();
    Mockito.doCallRealMethod().when(contactTranslator).translateToConnect(Mockito.any());
    Mockito.doCallRealMethod().when(custBranchTranslator).translateToConnect(Mockito.any());
    Mockito.doCallRealMethod().when(deliveryInfoTranslator).translateToConnect(Mockito.any());
    Mockito.doCallRealMethod().when(invoiceTypeTranslator).translateToConnect(Mockito.any());

    Mockito.when(paymentDataTranslator.translateToConnect(Mockito.any()))
    .thenReturn(PaymentMethodType.CARD);
  }

  @Test
  public void testConvertData() {
    final String expCustId = "dvsero";
    final Customer customer = converter.apply(outCustomer);

    System.out.println(SagJSONUtil.convertObjectToPrettyJson(customer));

    Assert.assertThat(customer.getNr(), Matchers.is(expCustId));
  }

}
