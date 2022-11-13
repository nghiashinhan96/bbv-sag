package com.sagag.services.stakis.erp.translator;

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
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.config.StakisConfigData;
import com.sagag.services.stakis.erp.config.StakisErpProperties;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfNote;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CisPaymentDataTranslatorTest {

  @InjectMocks
  private CisPaymentDataTranslator translator;

  @Mock
  private StakisErpProperties props;

  @Mock
  private StakisConfigData data;

  private OutCustomer outCustomer;

  private ArrayOfNote note;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();
    this.note = XmlUtils.getValueOpt(
        this.outCustomer.getCustomer().getValue().getNotes()).orElse(null);
  }

  @Test
  public void testTranslateData() {
    Mockito.when(props.getConfig()).thenReturn(data);
    Mockito.when(data.getPayment()).thenReturn(DataProvider.payments());
    PaymentMethodType paymentMethod = translator.translateToConnect(note);
    Assert.assertThat(paymentMethod, Matchers.is(PaymentMethodType.CASH));
  }
}
