package com.sagag.services.stakis.erp.converter;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.converter.impl.customer.CisCustomerCreditLimitConverterImpl;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CustomerCreditLimitConverterTest {

  @InjectMocks
  private CisCustomerCreditLimitConverterImpl converter;

  private OutCustomer outCustomer;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();
  }

  @Test
  public void testConvertData() {
    final CreditLimitInfo credit = converter.apply(outCustomer);

    System.out.println(SagJSONUtil.convertObjectToPrettyJson(credit));

    Assert.assertThat(credit, Matchers.notNullValue());
  }

}
