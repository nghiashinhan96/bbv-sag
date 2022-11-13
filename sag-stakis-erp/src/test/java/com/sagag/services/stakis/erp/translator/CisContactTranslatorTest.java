package com.sagag.services.stakis.erp.translator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.enums.StakisContactType;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfContact;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CisContactTranslatorTest {

  @InjectMocks
  private CisContactTranslator translator;

  private OutCustomer outCustomer;

  private ArrayOfContact arrayOfContact;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();

    this.arrayOfContact = XmlUtils.getValueOpt(
        this.outCustomer.getCustomer().getValue().getContacts()).orElse(null);
  }

  @Test
  public void testTranslateData() {
    Map<StakisContactType, List<ContactInfo>> contactMap =
        translator.translateToConnect(arrayOfContact);

    Assert.assertThat(contactMap.isEmpty(), Matchers.is(false));
  }

}
