package com.sagag.eshop.service.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.invoice.InvoiceHistoryPositionRepository;
import com.sagag.eshop.repo.api.invoice.InvoiceHistoryRepository;
import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.eshop.repo.entity.invoice.InvoiceHistoryPosition;
import com.sagag.eshop.service.helper.InvoiceHistoryConverter;
import com.sagag.eshop.service.helper.InvoiceHistoryPositionConverter;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Unit test class for SagsysInvoiceServiceImpl service.
 */
@RunWith(MockitoJUnitRunner.class)
public class InvoiceHistoryServiceImplTest {

  @InjectMocks
  private InvoiceHistoryServiceImpl invoiceHistoryService;

  @Mock
  private InvoiceHistoryRepository invoiceHistoryRepo;

  @Mock
  private InvoiceHistoryConverter invoiceHistoryConverter;

  @Mock
  private InvoiceHistoryPositionRepository invoiceHistoryPositionRepo;

  @Mock
  private InvoiceHistoryPositionConverter invoiceHistoryPositionConverter;

  @Test
  public void givenCustomerNr_ShouldReturnResult() {

    final String customerNr = "100";
    final String dateFrom = "2018-08-01";
    final String dateTo = "2018-08-26";

    when(invoiceHistoryRepo.findByDateAndCustomer(any(Integer.class), any(Date.class),
        any(Date.class), any(String.class))).thenReturn(new ArrayList<>());
    invoiceHistoryService.search(customerNr, dateFrom, dateTo);
    verify(invoiceHistoryRepo).findByDateAndCustomer(any(Integer.class), any(Date.class),
        any(Date.class), any(String.class));
  }

  @Test
  public void givenNotExistedInvoiceNr_ShouldReturnEmpty() {
    final String invoiceNr = "";
    final String orderNr = "11883821";
    when(invoiceHistoryRepo.findByInvoiceNr(any(Long.class), any(String.class)))
        .thenReturn(Lists.emptyList());
    final Optional<InvoiceDto> invoiceDetailOpt =
        invoiceHistoryService.getInvoiceDetail(null, invoiceNr, orderNr);
    verify(invoiceHistoryRepo).findByInvoiceNr(any(Long.class), any(String.class));
    Assert.assertFalse(invoiceDetailOpt.isPresent());
  }

  @Test
  public void givenValidInvoiceNr_ShouldReturnInvoiceDetail() {
    final String invoiceNr = "6119810";
    final String orderNr = "11883821";
    when(invoiceHistoryRepo.findByInvoiceNr(any(Long.class), any(String.class)))
        .thenReturn(Lists.newArrayList(new InvoiceHistory()));
    when(invoiceHistoryPositionRepo.findAllByInvoiceNr(any(Long.class)))
        .thenReturn(Lists.newArrayList(new InvoiceHistoryPosition()));
    when(invoiceHistoryPositionConverter.apply(any(InvoiceHistoryPosition.class)))
        .thenReturn(new InvoicePositionDto());
    when(invoiceHistoryConverter.apply(any(InvoiceHistory.class))).thenReturn(new InvoiceDto());
    final Optional<InvoiceDto> invoiceDetailOpt =
        invoiceHistoryService.getInvoiceDetail(null, invoiceNr, orderNr);
    verify(invoiceHistoryRepo).findByInvoiceNr(any(Long.class), any(String.class));
    verify(invoiceHistoryPositionRepo).findAllByInvoiceNr(any(Long.class));
    Assert.assertTrue(invoiceDetailOpt.isPresent());
  }

}
