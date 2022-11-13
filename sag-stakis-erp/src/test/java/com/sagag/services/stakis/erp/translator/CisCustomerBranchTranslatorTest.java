package com.sagag.services.stakis.erp.translator;

import java.io.IOException;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;
import com.sagag.services.stakis.erp.wsdl.cis.SalesOutletList;

@RunWith(SpringRunner.class)
public class CisCustomerBranchTranslatorTest {

  @InjectMocks
  private CisCustomerBranchTranslator translator;

  private OutCustomer outCustomer;

  private SalesOutletList salesOutletList;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();
    this.salesOutletList = XmlUtils.getValueOpt(
        this.outCustomer.getCustomer().getValue().getSalesOutletList()).orElse(null);
  }

  @Test
  public void testTranslateData() {
    Optional<CustomerBranch> branchOpt = translator.translateToConnect(salesOutletList);

    Assert.assertThat(branchOpt.isPresent(), Matchers.is(true));
  }
}
