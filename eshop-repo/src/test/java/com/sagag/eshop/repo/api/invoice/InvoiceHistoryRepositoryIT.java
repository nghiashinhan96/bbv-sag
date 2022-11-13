package com.sagag.eshop.repo.api.invoice;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.eshop.repo.enums.InvoiceHistorySource;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.DateUtils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Integration test class for role {@link InvoiceHistoryRepository}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class InvoiceHistoryRepositoryIT {

  @Autowired
  private InvoiceHistoryRepository repo;

  @Test
  public void findByDateAndCustomer_givernCustomerNr_shouldReturnResult() {
    Integer customerNr = 467143;
    String dateFrom = "2019-11-01 00:00:00.000";
    String dateTo = "2019-12-31 23:59:59.999";
    Date beginOfDateFrom = DateUtils.toDate(dateFrom, DateUtils.SWISS_DATE_TIME_PATTERN);
    Date endOfDateTo = DateUtils.toDate(dateTo, DateUtils.SWISS_DATE_TIME_PATTERN);

    final List<InvoiceHistory> invoices = repo.findByDateAndCustomer(customerNr, beginOfDateFrom,
        endOfDateTo, InvoiceHistorySource.SAGSYS.toString());

    Assert.assertThat(invoices, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
  }

}
