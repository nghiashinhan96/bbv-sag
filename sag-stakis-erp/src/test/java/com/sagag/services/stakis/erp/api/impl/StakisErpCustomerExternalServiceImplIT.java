package com.sagag.services.stakis.erp.api.impl;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.stakis.erp.StakisErpApplication;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StakisErpApplication.class)
@EshopIntegrationTest
@CzProfile
@Slf4j
public class StakisErpCustomerExternalServiceImplIT {

  @Autowired
  private StakisErpCustomerExternalServiceImpl service;

  @Test
  public void testGetActiveCustomerInfo() {
    final Optional<CustomerInfo> customerInfo = service
        .getActiveCustomerInfo(SupportedAffiliate.STAKIS_CZECH.getCompanyName(), "201906");
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(customerInfo.get()));
  }

}
