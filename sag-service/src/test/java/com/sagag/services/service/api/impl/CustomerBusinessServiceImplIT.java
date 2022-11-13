package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.CustomerBusinessService;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.TelephoneFormatException;
import com.sagag.services.service.request.CustomerSearchForm;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * IT for Customer Business.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class CustomerBusinessServiceImplIT {

  @Autowired
  private CustomerBusinessService customerBusService;

  @Test
  public void testSearchCustomersByFreeText()
      throws TelephoneFormatException, UserValidationException, ErpCustomerNotFoundException {
    final String text = "Kirchdorf an der Krems";
    final CustomerSearchForm form = new CustomerSearchForm();
    form.setAffiliate(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    form.setText(text);
    form.setOffset(0);
    form.setSize(SagConstants.MIN_PAGE_SIZE);

    final CustomerDataResultDto result = customerBusService.search(form);
    log.info("Result = {}", SagJSONUtil.convertObjectToPrettyJson(result));

    Assert.assertThat(result.hasRecommendCustomers(), Matchers.is(true));
    final Page<Customer> customers = result.getRecommendCustomers();
    boolean isMatchedCityName = customers.getContent().stream()
        .anyMatch(customer -> StringUtils.containsAny(customer.getCity(), text));
    Assert.assertThat(isMatchedCityName, Matchers.is(true));
  }

}
