package com.sagag.services.stakis.erp.translator;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.wsdl.cis.CustomerItem;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CisInvoiceTypeTranslatorTest {

  @InjectMocks
  private CisInvoiceTypeTranslator translator;

  private OutCustomer outCustomer;

  private CustomerItem custItem;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();
    this.custItem = this.outCustomer.getCustomer().getValue().getCustomerItems().getValue().getCustomerItem().get(0);
  }

  @Test
  public void testTranslateData() {
    ErpInvoiceTypeCode invoiceType = translator.translateToConnect(custItem);
    Assert.assertThat(invoiceType, Matchers.is(ErpInvoiceTypeCode.SINGLE_INVOICE));
  }
}
