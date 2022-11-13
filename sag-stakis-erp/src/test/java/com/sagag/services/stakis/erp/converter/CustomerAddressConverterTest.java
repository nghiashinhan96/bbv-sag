package com.sagag.services.stakis.erp.converter;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.stakis.erp.DataProvider;
import com.sagag.services.stakis.erp.converter.impl.customer.CisCustomerAddressConverterImpl;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
public class CustomerAddressConverterTest {

  @InjectMocks
  private CisCustomerAddressConverterImpl converter;

  private OutCustomer outCustomer;

  @Before
  public void setup() throws IOException, JAXBException {
    this.outCustomer = DataProvider
        .getFullGetCustomerResponseBody().getValue().getGetCustomerResult().getValue();
  }

  @Test
  public void testConvertData() {
    final List<Address> addresses = converter.apply(outCustomer);

    System.out.println(SagJSONUtil.convertObjectToPrettyJson(addresses));

    Assert.assertThat(addresses.isEmpty(), Matchers.is(false));
  }


}
