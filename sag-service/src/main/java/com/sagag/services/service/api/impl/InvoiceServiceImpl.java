package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.api.InvoiceHistoryService;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.InvoiceExternalService;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exporter.ExportStreamedResult;
import com.sagag.services.common.exporter.SupportedExportType;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.hazelcast.domain.order.OrderItemDetailDto;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.service.api.InvoiceService;
import com.sagag.services.service.invoice.InvoiceArchiveDocumentStream;
import com.sagag.services.service.invoice.InvoiceUrlBuillder;
import com.sagag.services.service.request.invoice.InvoiceSearchRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

  private static final String INVOICE_FILE_PATTERN = "invoice_";

  private static final int DEFAULT_MAX_DURATION = 30;

  @Autowired
  private OrderHistoryService orderHistoryService;

  @Autowired
  private InvoiceExternalService invoiceExtService;

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private InvoiceHistoryService invoiceHistoryService;

  @Autowired
  private InvoiceUrlBuillder invoiceUrlBuillder;

  @Autowired
  private ArticleConverter articleConverter;

  @Autowired
  private InvoiceArchiveDocumentStream invoiceArchiveDocStream;

  @Override
  public List<InvoiceDto> searchInvoices(UserInfo user, InvoiceSearchRequest request) {
    setDefaultDateSearch(request);

    final Supplier<List<InvoiceDto>> sagsysInvoicesSupplier = () -> invoiceHistoryService.search(
        user.getCustNrStr(), request.getDateFrom(), request.getDateTo());
    final Supplier<List<InvoiceDto>> externalInvoiceSupplier = () -> invoiceExtService
        .searchInvoices(user.getCompanyName(), user.getCustNrStr(),
            request.toAxInvoiceSearchRequest());

    final List<InvoiceDto> invoices = BooleanUtils.isTrue(request.getOldInvoice())
        ? sagsysInvoicesSupplier.get() : externalInvoiceSupplier.get();

    if (!user.isNormalUserRole()) {
      return invoices;
    }

    final List<String> orderNrs =
        invoices.stream().map(InvoiceDto::getOrderNr).distinct().collect(Collectors.toList());
    final List<String> availableOrderNrs =
        orderHistoryService.searchAvailableOrderNrs(user.getId(), orderNrs);
    invoices.removeIf(invoice -> !availableOrderNrs.contains(invoice.getOrderNr()));
    return invoices;
  }

  private void setDefaultDateSearch(InvoiceSearchRequest request) {
    String dateFrom = request.getDateFrom();
    String defaultDateFrom = StringUtils.isBlank(dateFrom)
        ? Instant.now().minus(Duration.ofDays(DEFAULT_MAX_DURATION)).toString()
        : dateFrom;
    request.setDateFrom(defaultDateFrom);

    String dateTo = request.getDateTo();
    String defaultDateTo = StringUtils.isBlank(dateTo) ? Instant.now().toString() : dateTo;
    request.setDateTo(defaultDateTo);
  }

  @Override
  public Optional<InvoiceDto> getInvoiceDetail(UserInfo user, String invoiceNr, String orderNr,
      boolean simpleMode, boolean oldInvoice) {

    if (oldInvoice) {
      return invoiceHistoryService.getInvoiceDetail(user, invoiceNr, orderNr);
    }
    orderNr = simpleMode ? null : orderNr;
    final Optional<InvoiceDto> invoice = invoiceExtService.getInvoiceDetail(user.getCompanyName(),
        user.getCustNrStr(), invoiceNr, orderNr, user.getInvoiceAddress());

    if (!invoice.filter(InvoiceDto::hasPositions).isPresent()) {
      return invoice;
    }
    return invoice.map(articleInfoConsumer());
  }

  private UnaryOperator<InvoiceDto> articleInfoConsumer() {
    return invoice -> {
      final Optional<OrderHistory> orderHistory =
          orderHistoryService.searchOrderByNr(invoice.getOrderNr());
      if (!orderHistory.isPresent()) {
        articleDetailConsumer(Optional.empty()).accept(invoice);
        return invoice;
      }

      final OrderHistory historyItem = orderHistory.get();
      final OrderInfoDto orderInfo = OrderInfoDto.createOrderInfoDtoFromJson(
          historyItem.getOrderInfoJson());
      orderInfoConsumer(orderInfo).accept(invoice, historyItem.getId());
      articleDetailConsumer(Optional.of(orderInfo)).accept(invoice);
      return invoice;
    };
  }

  private BiConsumer<InvoiceDto, Long> orderInfoConsumer(final OrderInfoDto orderInfo) {
    return (invoice, orderHistoryId) -> {
      invoice.setOrderHistoryId(orderHistoryId);
      invoice.setInvoiceType(orderInfo.getInvoiceTypeCode());
      invoice.setVehicles(orderInfo.getVehicleInfosFromOrderItem());
    };
  }

  private Consumer<InvoiceDto> articleDetailConsumer(final Optional<OrderInfoDto> orderInfo) {
    return invoice -> {
      final List<String> pimIds = invoice.getPimIds();
      if (CollectionUtils.isEmpty(pimIds)) {
        return;
      }
      final Pageable pageable = PageUtils.defaultPageable(pimIds.size());
      // #4274 : Here is not apply any locks so
      final Page<ArticleDocDto> articles =
          articleSearchService.searchArticlesByIdSagSyses(pimIds, pageable)
              .map(articleConverter);
      Map<String, ArticleDocDto> articlesMap = articles.getContent().stream()
          .collect(Collectors.toMap(ArticleDocDto::getIdSagsys, Function.identity()));
      invoice.getPositions().forEach(invoicePositionConsumer(articlesMap, orderInfo));
    };
  }

  private static Consumer<InvoicePositionDto> invoicePositionConsumer(
      final Map<String, ArticleDocDto> articles, final Optional<OrderInfoDto> orderInfo) {
    return position -> {
      final ArticleDocDto article = articles.get(position.getArticleId());
      if (article != null) {
        position.setArticleNr(article.getArtnrDisplay());
        position.setArticleTitle(article.getArticleTitle());
      }
      orderInfo.ifPresent(info -> info.getItems().stream()
          .filter(filterOrderItemByArtNr(position)).findFirst()
          .ifPresent(item -> position.setVehicleInfo(item.getVehicle().getVehicleInfo())));
    };
  }

  private static Predicate<OrderItemDetailDto> filterOrderItemByArtNr(final InvoicePositionDto position) {
    return orderItem -> StringUtils.equalsIgnoreCase(
        orderItem.getArticle().getArtnrDisplay(), position.getArticleNr());
  }

  @Override
  public ExportStreamedResult streamInvoicePdf(SupportedAffiliate affiliate, String custNr,
    String invoiceNr, boolean oldInvoice, String orderNr) throws IOException {
    final String invoiceUrl = invoiceUrlBuillder.build(affiliate, custNr, invoiceNr, oldInvoice,
        orderNr);
    log.info("invoiceUrl: {}", invoiceUrl);
    byte[] bytes = invoiceArchiveDocStream.streamInvoiceArchiveDoc(invoiceUrl);
    final String fileName = INVOICE_FILE_PATTERN + invoiceNr;
    return ExportStreamedResult.of(bytes, fileName, SupportedExportType.PDF, false);
  }

}
