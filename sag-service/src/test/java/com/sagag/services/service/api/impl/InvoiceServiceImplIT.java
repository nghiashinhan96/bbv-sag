package com.sagag.services.service.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.service.SagServiceApplication;
import com.sagag.services.service.api.InvoiceService;
import com.sagag.services.service.request.invoice.InvoiceSearchRequest;
import com.sagag.services.service.user.cache.ISyncUserLoader;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Slf4j
@Transactional
@Ignore("Because cannot found current customer number of this user id")
public class InvoiceServiceImplIT {

  // CustNr = 1100005
  private static final long USER_ID_USER_AX_AD = 26l;

  @Autowired
  private ISyncUserLoader userLoader;
  @Autowired
  private InvoiceService service;

  private UserInfo user;

  @Before
  public void init() {
    if (user == null) {
      user = userLoader.load(USER_ID_USER_AX_AD, StringUtils.EMPTY,
          StringUtils.EMPTY, Optional.empty());
    }
  }

  @Test
  public void shouldSearchInvoices() {
    InvoiceSearchRequest request = new InvoiceSearchRequest();
    request.setDateFrom("2018-11-18");
    request.setDateTo("2018-12-19");
    final List<InvoiceDto> invoices = service.searchInvoices(user, request);
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(invoices));
    Assert.assertThat(invoices.size(), Matchers.greaterThanOrEqualTo(1));
  }
}
