package com.sagag.services.stakis.erp.client;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.stakis.erp.StakisErpApplication;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StakisErpApplication.class })
@EshopIntegrationTest
public class StakisErpCisClientIT {

  @Autowired
  private StakisErpCisClient client;

  @Test
  public void testCustomerInfo() {
    final String customerId = "201906";
    final String lang = "de";
    OutCustomer customer = client.getCustomer(StringUtils.EMPTY, customerId, lang);
    Assert.assertThat(customer, Matchers.notNullValue());
  }

}
