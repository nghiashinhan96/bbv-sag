package com.sagag.services.service.invoice;

import com.sagag.eshop.repo.api.invoice.InvoiceHistoryRepository;
import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.eshop.repo.enums.InvoiceHistorySource;
import com.sagag.services.article.api.InvoiceExternalService;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceUrlBuillderTest {

  @InjectMocks
  private InvoiceUrlBuillder invoiceUrlBuillder;

  @Mock
  private InvoiceExternalService axInvoiceService;

  @Mock
  private InvoiceHistoryRepository invoiceHistoryRepo;

  @Test
  public void givenInvoiceNrOldInvoice_shouldReturnUrl() {

    SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_CH;
    String custNr = "1";
    String invoiceNr = "1";
    boolean oldInvoice = true;
    String orderNr = "2";
    ReflectionTestUtils.setField(invoiceUrlBuillder, "sagsysInvoiceUrl", "url");
    Mockito
        .when(invoiceHistoryRepo.findByInvoiceNr(Long.valueOf(invoiceNr),
            InvoiceHistorySource.SAGSYS.toString()))
        .thenReturn(Arrays
            .asList(InvoiceHistory.builder().orderNr(2l).invoiceNr(1l).docId("1234567").build()));
    String result = invoiceUrlBuillder.build(affiliate, custNr, invoiceNr, oldInvoice, orderNr);
    Assert.assertNotNull(result);
  }


  @Test
  public void givenInvoiceNr_shouldReturnUrl() {

    SupportedAffiliate affiliate = SupportedAffiliate.DERENDINGER_CH;
    String custNr = "1";
    String invoiceNr = "1";
    boolean oldInvoice = false;
    String orderNr = "2";

    Mockito.when(axInvoiceService.getInvoicePdfUrl(affiliate.getCompanyName(), custNr, invoiceNr))
        .thenReturn(Optional.of(StringUtils.EMPTY));

    axInvoiceService.getInvoicePdfUrl(affiliate.getCompanyName(), custNr, invoiceNr);

    String result = invoiceUrlBuillder.build(affiliate, custNr, invoiceNr, oldInvoice, orderNr);
    Assert.assertNotNull(result);
  }



}
