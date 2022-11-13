package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.invoice.InvoiceHistoryPositionRepository;
import com.sagag.eshop.repo.api.invoice.InvoiceHistoryRepository;
import com.sagag.eshop.repo.entity.invoice.InvoiceHistory;
import com.sagag.eshop.repo.enums.InvoiceHistorySource;
import com.sagag.eshop.service.api.InvoiceHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.helper.InvoiceHistoryConverter;
import com.sagag.eshop.service.helper.InvoiceHistoryPositionConverter;
import com.sagag.eshop.service.helper.InvoiceHistoryToInvoicePositionConverter;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceHistoryServiceImpl implements InvoiceHistoryService {

  @Autowired
  private InvoiceHistoryRepository invoiceHistoryRepo;

  @Autowired
  private InvoiceHistoryConverter invoiceHistoryConverter;

  @Autowired
  private InvoiceHistoryPositionRepository invoiceHistoryPositionRepo;

  @Autowired
  private InvoiceHistoryPositionConverter invoiceHistoryPositionConverter;

  @Autowired
  private InvoiceHistoryToInvoicePositionConverter sagsysInvoicePositionConverter;

  @Override
  @Transactional
  public List<InvoiceDto> search(String customerNr, String dateFrom, String dateTo) {
    Asserts.notEmpty(customerNr, "customerNr is required");
    Asserts.notEmpty(dateFrom, "dateFrom is required");
    Asserts.notEmpty(dateTo, "dateTo is required");
    Date beginOfDateFrom = DateUtils.toDate(dateFrom + SagConstants.SPACE + DateUtils.BEGIN_OF_DAY,
        DateUtils.SWISS_DATE_TIME_PATTERN);
    Date endOfDateTo = DateUtils.toDate(dateTo + SagConstants.SPACE + DateUtils.END_OF_DAY,
        DateUtils.SWISS_DATE_TIME_PATTERN);

    final long start = System.currentTimeMillis();
    final Long custNr = Long.valueOf(customerNr);
    List<InvoiceHistory> invoices = invoiceHistoryRepo.findByDateAndCustomer(custNr.intValue(),
        beginOfDateFrom, endOfDateTo, InvoiceHistorySource.SAGSYS.toString());
    log.debug("SagsysInvoiceServiceImpl-> search-> findByDateAndCustomer {} ms",
        System.currentTimeMillis() - start);

    return invoices.stream().map(invoiceHistoryConverter).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Optional<InvoiceDto> getInvoiceDetail(UserInfo user, String invoiceNr, String orderNr) {
    Asserts.notEmpty(invoiceNr, "invoiceNr is required");
    final Long invoiceNrValue = Long.valueOf(invoiceNr);
    List<InvoiceHistory> invoices =
        invoiceHistoryRepo.findByInvoiceNr(invoiceNrValue, InvoiceHistorySource.SAGSYS.toString());
    if (CollectionUtils.isEmpty(invoices)) {
      return Optional.empty();
    }
    InvoiceHistory selectedOrder = invoices.stream()
        .filter(i -> Objects.nonNull(i.getOrderNr()) && i.getOrderNr().toString().equals(orderNr))
        .findFirst().orElse(invoices.get(0));

    InvoiceDto invoiceDto = invoiceHistoryConverter.apply(selectedOrder);

    final List<InvoicePositionDto> positions;
    if (user.getSupportedAffiliate() != null
        && user.getSupportedAffiliate().isSagCzAffiliate()) {
      positions = invoiceHistoryPositionRepo.findAllByInvoiceNr(invoiceNrValue)
          .stream().map(invoiceHistoryPositionConverter).collect(Collectors.toList());
    } else {
      positions = invoices.stream().map(sagsysInvoicePositionConverter)
          .collect(Collectors.toList());
    }

    invoiceDto.setPositions(positions);

    Address address = Address.builder().street(selectedOrder.getStreet()).build();
    invoiceDto.setAddress(address);

    return Optional.of(invoiceDto);
  }

}
