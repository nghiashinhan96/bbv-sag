package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@Slf4j
@EshopIntegrationTest
@Transactional
public class InvoiceTypeRepositoryIT {

  @Autowired
  private InvoiceTypeRepository repository;

  @Test
  public void testFindInvoiceTypeCodeByInvoiceTypeName() {
    final String invoiceTypeName = "EINZELFAKT".toLowerCase();
    final String expectedConnectInvoiceTypeCode = "SINGLE_INVOICE";

    Optional<String> result = repository.findInvoiceTypeCodeByInvoiceTypeName(invoiceTypeName);
    Assert.assertThat(result.isPresent(), Matchers.is(true));
    log.debug("Value = {}", SagJSONUtil.convertObjectToPrettyJson(result));
    result.ifPresent(value -> Assert.assertThat(value, Matchers.is(expectedConnectInvoiceTypeCode)));
  }
}
