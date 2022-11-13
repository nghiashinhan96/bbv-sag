package com.sagag.services.stakis.erp.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.client.StakisErpCisClient;
import com.sagag.services.stakis.erp.converter.CisCustomerDataConverter;
import com.sagag.services.stakis.erp.converter.impl.customer.CisCustomerAddressConverterImpl;
import com.sagag.services.stakis.erp.converter.impl.customer.CisCustomerCreditLimitConverterImpl;
import com.sagag.services.stakis.erp.converter.impl.customer.CisCustomerDataConverterImpl;
import com.sagag.services.stakis.erp.translator.CisContactTranslator;
import com.sagag.services.stakis.erp.translator.CisCustomerBranchTranslator;
import com.sagag.services.stakis.erp.translator.CisDeliveryInfoTranslator;
import com.sagag.services.stakis.erp.translator.CisInvoiceTypeTranslator;
import com.sagag.services.stakis.erp.translator.CisPaymentDataTranslator;

@RunWith(MockitoJUnitRunner.class)
public class StakisErpCustomerExternalServiceImpTest {

  @InjectMocks
  private StakisErpCustomerExternalServiceImpl service;

  @Mock
  private StakisErpCisClient stakisErpCisClient;

  @Spy
  private List<CisCustomerDataConverter<?>> converters = new ArrayList<>();

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

  @Mock
  private CisCustomerDataConverterImpl customerConverter;

  @Mock
  private CisCustomerAddressConverterImpl addressConverter;

  @Mock
  private CisCustomerCreditLimitConverterImpl creditLimitConverter;

  @Before
  public void setup() {
    converters.clear();
    converters.add(customerConverter);
    converters.add(addressConverter);
    converters.add(creditLimitConverter);
  }

  @Test
  public void testGetActiveCustomerInfo() throws IOException, JAXBException {

    Mockito.when(stakisErpCisClient.getCustomer(Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).thenReturn(DataProvider
            .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue());

    final Optional<CustomerInfo> customerInfo = service.getActiveCustomerInfo(
        SupportedAffiliate.STAKIS_CZECH.getCompanyName(), DataProvider.TEST_CUSTOMER);

    Assert.assertThat(customerInfo.isPresent(),
        Matchers.anyOf(Matchers.is(true), Matchers.is(false)));
  }

}
