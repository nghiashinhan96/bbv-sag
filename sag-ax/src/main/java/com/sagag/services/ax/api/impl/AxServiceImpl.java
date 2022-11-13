package com.sagag.services.ax.api.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;

import com.sagag.eshop.repo.api.BranchRepository;
import com.sagag.eshop.repo.entity.Branch;
import com.sagag.eshop.repo.specification.BranchSpecifications;
import com.sagag.services.article.api.BranchExternalService;
import com.sagag.services.article.api.CourierExternalService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.InvoiceExternalService;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.ReturnExternalOrderService;
import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.article.api.request.InvoiceExternalSearchRequest;
import com.sagag.services.article.api.request.OrderStatusRequest;
import com.sagag.services.article.api.request.returnorder.AxReturnOrderRequest;
import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.ax.client.AxClient;
import com.sagag.services.ax.converter.AxCustomerInfoConverter;
import com.sagag.services.ax.converter.AxInvoiceConverter;
import com.sagag.services.ax.converter.AxInvoicesConverter;
import com.sagag.services.ax.domain.AxAddress;
import com.sagag.services.ax.domain.AxAddressResourceSupport;
import com.sagag.services.ax.domain.AxBranch;
import com.sagag.services.ax.domain.AxBranchesResourceSupport;
import com.sagag.services.ax.domain.AxCourier;
import com.sagag.services.ax.domain.AxCouriersResourceSupport;
import com.sagag.services.ax.domain.AxCreditLimitInfo;
import com.sagag.services.ax.domain.AxCustomer;
import com.sagag.services.ax.domain.AxCustomerInfo;
import com.sagag.services.ax.domain.AxOrderConfirmation;
import com.sagag.services.ax.domain.AxOrderHistory;
import com.sagag.services.ax.domain.AxOrderPositions;
import com.sagag.services.ax.domain.AxOrderStatusConfirmation;
import com.sagag.services.ax.domain.invoice.AxInvoice;
import com.sagag.services.ax.domain.invoice.AxInvoicePDF;
import com.sagag.services.ax.domain.invoice.AxInvoicePosition;
import com.sagag.services.ax.domain.invoice.AxInvoicePositions;
import com.sagag.services.ax.domain.invoice.AxInvoices;
import com.sagag.services.ax.domain.returnorder.AxReturnOrder;
import com.sagag.services.ax.domain.returnorder.AxReturnOrderBatchJobResult;
import com.sagag.services.ax.domain.returnorder.AxReturnOrderBatchJobs;
import com.sagag.services.ax.domain.returnorder.AxTransactionReference;
import com.sagag.services.ax.domain.returnorder.AxTransactionReferences;
import com.sagag.services.ax.exception.AxResultNotFoundException;
import com.sagag.services.ax.exception.translator.AxExternalExceptionTranslator;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.ax.translator.AxReturnOrderNameTranslator;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.ax.translator.PaymentMethodTranslator;
import com.sagag.services.ax.utils.AxAddressUtils;
import com.sagag.services.ax.utils.AxConstants;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.eshop.branch.dto.BranchSearchRequestCriteria;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.domain.sag.external.Courier;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.invoice.InvoicePositionDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * The service to get info from Ax Connection.
 * </p>
 *
 */
@Service
@Slf4j
@AxProfile
public class AxServiceImpl extends AxProcessor implements CustomerExternalService,
    InvoiceExternalService,
    ReturnExternalOrderService,
    OrderExternalService,
    BranchExternalService,
    CourierExternalService {

  @Autowired
  private AxClient axClient;

  @Autowired
  private AxPaymentTypeTranslator axPaymentTypeTranslator;

  @Autowired
  private AxSendMethodTranslator axSendMethodTranslator;

  @Autowired
  private PaymentMethodTranslator axPaymentMethodTranslator;

  @Autowired
  private AxReturnOrderNameTranslator axReturnOrderNameTranslator;

  @Autowired
  private AxInvoiceConverter axInvoiceConverter;

  @Autowired
  private AxInvoicesConverter axInvoicesConverter;

  @Autowired
  private AxCustomerInfoConverter axCustomerInfoConverter;

  @Autowired
  @Qualifier("axCustomerInternalServerExceptionTranslator")
  private AxExternalExceptionTranslator axCustomerInternalServerExceptionTranslator;

  @Autowired
  private BranchRepository branchRepo;

  @Override
  public OrderConfirmation createOrder(String companyName, ExternalOrderRequest request) {
    final ExternalOrderRequest translatedOrderRequest = translateOrderRequest(request);
    Function<String, ResponseEntity<AxOrderConfirmation>> function =
        token -> axClient.createOrder(token, companyName, translatedOrderRequest);
    return retryIfExpiredToken(function).getBody().toOrderConfirmationDto();
  }

  @Override
  public OrderConfirmation createBasket(String companyName, ExternalOrderRequest request) {
    final ExternalOrderRequest translatedOrderRequest = translateOrderRequest(request);
    Function<String, ResponseEntity<AxOrderConfirmation>> function =
        token -> axClient.createShoppingBasket(token, companyName, translatedOrderRequest);
    return retryIfExpiredToken(function).getBody().toOrderConfirmationDto();
  }

  @Override
  public OrderConfirmation createOffer(String companyName, ExternalOrderRequest request) {

    final ExternalOrderRequest translatedOrderRequest = translateOrderRequest(request);
    Function<String, ResponseEntity<AxOrderConfirmation>> function =
        token -> axClient.createOffer(token, companyName, translatedOrderRequest);
    return retryIfExpiredToken(function).getBody().toOrderConfirmationDto();
  }

  private ExternalOrderRequest translateOrderRequest(final ExternalOrderRequest request) {
    request.setSendMethodCode(axSendMethodTranslator.translateToAx(request.getSendMethodCode()));
    request.setPaymentMethod(axPaymentMethodTranslator.translateToAx(request.getPaymentMethod()));
    return request;
  }

  @Override
  public Optional<Customer> findCustomerByNumber(String companyName, String customerNr) {
    Function<String, ResponseEntity<AxCustomer>> function = token -> getOrThrow(
        () -> axClient.getCustomerByNr(token, companyName, String.valueOf(customerNr)),
        axCustomerInternalServerExceptionTranslator);
    final AxCustomer axCustomer = retryIfExpiredToken(function).getBody();
    return Optional.of(axCustomer).map(axCustomerInfoConverter);
  }

  @Override
  public Optional<ExternalOrderHistory> getExternalOrderByHrefLink(String relativeUrl) {
    Function<String, ResponseEntity<AxOrderHistory>> function =
        token -> axClient.getOrdersOfCustomerByHrefLink(token, relativeUrl);
    ResponseEntity<AxOrderHistory> ordersRes = retryIfExpiredToken(function);
    if (!ordersRes.hasBody() || !ordersRes.getBody().hasOrders()) {
      return Optional.empty();
    }
    AxOrderHistory axOrdersResBody = ordersRes.getBody();
    return Optional.of(axOrdersResBody.toExternalOrderHistoryDto());
  }

  @Override
  public Optional<ExternalOrderHistory> getExternalOrderHistoryOfCustomer(String companyName,
      String customerNr, ExternalOrderHistoryRequest request, Integer page) {
    ResponseEntity<AxOrderHistory> ordersRes;
    try {
      Function<String, ResponseEntity<AxOrderHistory>> function =
          token -> axClient.getOrdersOfCustomer(token, companyName, customerNr,
              request.getOrderNumber(), request.getFrom(), request.getTo(), page);
      ordersRes = retryIfExpiredToken(function);
      if (!ordersRes.hasBody() || !ordersRes.getBody().hasOrders()) {
        return Optional.empty();
      }
    } catch (HttpClientErrorException ex) {
      if (HttpStatus.NOT_FOUND.equals(ex.getStatusCode())) {
        log.debug("Error: {}", ex);
        return Optional.empty();
      }
      throw ex;
    }
    AxOrderHistory axOrdersResBody = ordersRes.getBody();
    return Optional.of(axOrdersResBody.toExternalOrderHistoryDto());
  }

  @Override
  public Optional<ExternalOrderPositions> getOrderPositions(String companyName, String customerNr,
      String orderNr) {
    Function<String, ResponseEntity<AxOrderPositions>> function =
        token -> axClient.getOrderPositions(token, companyName, customerNr, orderNr);
    ResponseEntity<AxOrderPositions> axOrderRes = retryIfExpiredToken(function);
    if (!axOrderRes.hasBody()) {
      return Optional.empty();
    }
    return Optional.of(axOrderRes.getBody().toExternalOrderPositionsDto());
  }

  @Override
  public Optional<Address> findCustomerAddressById(String company, String customerNr,
      String addressId) {
    Function<String, ResponseEntity<AxAddress>> function =
        token -> axClient.getAddressById(token, company, String.valueOf(customerNr), addressId);
    final ResponseEntity<AxAddress> address = retryIfExpiredToken(function);
    if (!address.hasBody()) {
      return Optional.empty();
    }
    final AxAddress axAddress = address.getBody();
    if (!AxAddressUtils.filterActiveDeliveryOrInvoiceAddress().test(axAddress)) {
      return Optional.empty();
    }
    return Optional.of(axAddress.toAddressDto());
  }

  @Override
  public Optional<CustomerBranch> searchBranchById(String companyName, String branchId) {
    Function<String, ResponseEntity<AxBranch>> function =
        token -> axClient.getBranchById(token, companyName, branchId);
    ResponseEntity<AxBranch> branchRes = retryIfExpiredToken(function);
    if (!branchRes.hasBody() || !branchRes.getBody().hasBranch()) {
      return Optional.empty();
    }
    return Optional.of(branchRes.getBody().toCustomerBranchDto());
  }

  @Override
  public List<CustomerBranch> getCustomerBranches(final String companyName,
      final String defaultBranchId, boolean isSaleOnBehalf) {
    Function<String, ResponseEntity<AxBranchesResourceSupport>> function =
        token -> axClient.getBranches(token, companyName);
    final ResponseEntity<AxBranchesResourceSupport> axBranches = retryIfExpiredToken(function);
    if (!axBranches.hasBody() || !axBranches.getBody().hasBranches()) {
      return Collections.emptyList();
    }
    List<CustomerBranch> customerBranches= axBranches.getBody().getBranches().stream().map(AxBranch::toCustomerBranchDto)
        .sorted(defaultBranchFirst(defaultBranchId)).collect(Collectors.toList());
    final boolean hideFromCustomers = !isSaleOnBehalf;
    final boolean hideFromSales = isSaleOnBehalf;
    BranchSearchRequestCriteria criteria = BranchSearchRequestCriteria.builder()
        .hideFromCustomers(hideFromCustomers).hideFromSales(hideFromSales).build();
    final Specification<Branch> specForBranch =
        BranchSpecifications.searchBranchByCriteria(criteria);
    List<String> branchNrs = branchRepo.findAll(specForBranch).stream()
        .map(branch -> String.valueOf(branch.getBranchNr())).collect(Collectors.toList());

    return customerBranches.stream().filter(branch -> branchNrs.contains(branch.getBranchId()))
        .collect(Collectors.toList());
  }

  @Override
  public List<CustomerBranch> getBranches(final String companyName) {
    Function<String, ResponseEntity<AxBranchesResourceSupport>> function =
        token -> axClient.getBranches(token, companyName);
    final ResponseEntity<AxBranchesResourceSupport> axBranches = retryIfExpiredToken(function);
    if (!axBranches.hasBody() || !axBranches.getBody().hasBranches()) {
      return Collections.emptyList();
    }
    return axBranches.getBody().getBranches().stream().map(AxBranch::toCustomerBranchDto)
        .collect(Collectors.toList());
  }

  private static Comparator<CustomerBranch> defaultBranchFirst(final String defaultBranchId) {
    return (branch1, branch2) -> {
      if (defaultBranchId.equals(branch1.getBranchId())) {
        return -1;
      }
      return defaultBranchId.equals(branch2.getBranchId()) ? 1 : 0;
    };
  }

  @Override
  public Optional<CreditLimitInfo> getCreditLimitInfoByCustomer(String compName, String custNr) {
    Function<String, ResponseEntity<AxCreditLimitInfo>> function =
        token -> axClient.getCreditLimitByCustomerNr(token, compName, custNr);
    final ResponseEntity<AxCreditLimitInfo> axCreditLimitInfoRes = retryIfExpiredToken(function);
    if (!axCreditLimitInfoRes.hasBody()) {
      return Optional.empty();
    }
    return Optional.of(axCreditLimitInfoRes.getBody());
  }

  @Override
  public boolean updateSalesOrderStatus(String compName, OrderStatusRequest request) {
    Function<String, ResponseEntity<AxOrderStatusConfirmation>> function =
        token -> axClient.updateSalesOrderStatus(token, compName, request);
    return retryIfExpiredToken(function).getBody().isProcessStatusChanged();
  }

  @Override
  public Optional<ExternalOrderHistory> getOrderDetailByOrderNr(String compName, String custNr,
      String orderNr) {
    return getExternalOrderHistoryOfCustomer(compName, custNr,
        ExternalOrderHistoryRequest.builder().orderNumber(orderNr).build(),
        NumberUtils.INTEGER_ONE);
  }

  @Override
  public List<Address> searchCustomerAddresses(String companyName, String customerNr) {
    Function<String, ResponseEntity<AxAddressResourceSupport>> function =
        token -> axClient.getAddressesOfCustomer(token, companyName, customerNr);
    ResponseEntity<AxAddressResourceSupport> addressesRes = retryIfExpiredToken(function);
    if (!addressesRes.hasBody() || !addressesRes.getBody().hasAddresses()) {
      return Collections.emptyList();
    }

    return addressesRes.getBody().getAddresses().stream()
        .filter(AxAddressUtils.filterActiveDeliveryOrInvoiceAddress()).map(AxAddress::toAddressDto)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<CustomerInfo> getActiveCustomerInfo(String compName, String custNr) {
    if (StringUtils.isAnyBlank(compName, custNr)) {
      return Optional.empty();
    }
    final Optional<Customer> custOpt = findCustomerByNumber(compName, custNr);
    if (!custOpt.filter(Customer::isActive).isPresent()) {
      return Optional.empty();
    }
    final Customer customer = custOpt.get();

    final AxCustomerInfo info = new AxCustomerInfo();
    info.setCustomer(customer);
    info.setAddresses(searchCustomerAddresses(compName, custNr));
    return Optional.of(info);
  }

  @Override
  public List<TransactionReferenceDto> searchTransactionReferences(String compName, String param) {
    Function<String, ResponseEntity<AxTransactionReferences>> function =
        token -> getOrDefaultThrow(() -> axClient.getTransactionReferences(token, compName, param));
    ResponseEntity<AxTransactionReferences> res = retryIfExpiredToken(function);
    return res.getBody().getTransactions().stream().map(transactionRefConverter())
        .collect(Collectors.toList());
  }

  private Function<AxTransactionReference, TransactionReferenceDto> transactionRefConverter() {
    return transaction -> {
      final TransactionReferenceDto transactionRefDto = transaction.toDto();
      final PaymentMethodType paymentMethodType =
          axPaymentTypeTranslator.translateToConnect(transaction.getAxPaymentType());
      transactionRefDto.setPaymentType(paymentMethodType);
      return transactionRefDto;
    };
  }

  @Override
  public ReturnOrderDto createReturnOrder(String compName, ReturnedOrderRequestBody request) {
    Assert.notNull(request, "request body must not be null");
    Assert.isTrue(request.isValidToExecute(), "request body is not valid to execute");
    final AxReturnOrderRequest axRequest = request.toAxRequest();
    axRequest.setReturnOrderName(axReturnOrderNameTranslator.translateToAx(request.getPositions()));
    Function<String, ResponseEntity<AxReturnOrder>> function =
        token -> getOrDefaultThrow(() -> axClient.createReturnOrder(token, compName, axRequest));
    return retryIfExpiredToken(function).getBody().toDto();
  }

  @Override
  public Optional<ReturnOrderBatchJobsDto> searchReturnOrderBatchJobStatus(String compName,
      List<String> batchJobIds) {
    final String joinedBatchJobIds =
        StringUtils.join(CollectionUtils.emptyIfNull(batchJobIds), SagConstants.COMMA);
    if (StringUtils.isBlank(joinedBatchJobIds)) {
      return Optional.empty();
    }
    Function<String, ResponseEntity<AxReturnOrderBatchJobs>> function = token -> getOrDefaultThrow(
        () -> axClient.searchReturnOrderBatchJob(token, compName, joinedBatchJobIds));
    return Optional.ofNullable(retryIfExpiredToken(function).getBody().toDto());
  }

  @Override
  public Optional<ReturnOrderBatchJobsResultDto> searchReturnOrderNumber(String compName,
      List<String> journalIds) {
    final String joinedJournalIds =
        StringUtils.join(CollectionUtils.emptyIfNull(journalIds), SagConstants.COMMA);
    if (StringUtils.isBlank(joinedJournalIds)) {
      return Optional.empty();
    }
    Function<String, ResponseEntity<AxReturnOrderBatchJobResult>> function =
        token -> getOrDefaultThrow(
            () -> axClient.searchReturnOrderNumber(token, compName, joinedJournalIds));
    return Optional.ofNullable(retryIfExpiredToken(function).getBody().toDto());
  }

  @Override
  public List<InvoiceDto> searchInvoices(final String compName, final String custNr,
      final InvoiceExternalSearchRequest request) {
    Function<String, ResponseEntity<AxInvoices>> function =
        token -> getOrDefaultThrow(() -> axClient.getInvoices(token, compName, custNr, request));
    final ResponseEntity<AxInvoices> response = retryIfExpiredToken(function);
    final List<InvoiceDto> invoices = response.getBody().getInvoices().stream()
        .map(axInvoiceConverter).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(invoices)) {
      return Collections.emptyList();
    }

    for (InvoiceDto invoice : invoices) {
      invoice.setPositions(
          getInvoicePositions(compName, custNr, invoice.getInvoiceNr(), StringUtils.EMPTY));
    }
    return axInvoicesConverter.apply(invoices);
  }

  @Override
  public Optional<InvoiceDto> getInvoiceDetail(final String compName, final String custNr,
      final String invoiceNr, final String orderNr, final Optional<Address> invoiceAddress) {
    Function<String, ResponseEntity<AxInvoice>> function = token -> getOrDefaultThrow(
        () -> axClient.getInvoiceByNr(token, compName, custNr, invoiceNr));
    final ResponseEntity<AxInvoice> axInvoice = retryIfExpiredToken(function);
    if (!axInvoice.hasBody()) {
      return Optional.empty();
    }

    final List<InvoicePositionDto> positions =
        getInvoicePositions(compName, custNr, invoiceNr, orderNr);
    if (CollectionUtils.isEmpty(positions)) {
      return Optional.empty();
    }
    final InvoiceDto invoice = axInvoiceConverter.apply(axInvoice.getBody());
    invoice.setPositions(positions);
    invoiceAddress.ifPresent(invoice::setAddress);
    return axInvoicesConverter.apply(Arrays.asList(invoice)).stream().findFirst();
  }

  private List<InvoicePositionDto> getInvoicePositions(final String compName, final String custNr,
      final String invoiceNr, final String orderNr) {
    List<InvoicePositionDto> listInvoicePosition = new ArrayList<>();
    for (int page = 0; listInvoicePosition.size() >= page * AxConstants.DEFAULT_SIZE_OF_PAGE; page++) {
      try {
        listInvoicePosition.addAll(getInvoicePositions(compName, custNr, invoiceNr, orderNr, page+1));
      } catch (AxResultNotFoundException exception) {
        break;
      }
    }
    return listInvoicePosition;
  }

  private List<InvoicePositionDto> getInvoicePositions(final String compName, final String custNr,
      final String invoiceNr, final String orderNr, final int page) {
    Function<String, ResponseEntity<AxInvoicePositions>> function = token -> getOrDefaultThrow(
            () -> axClient.getInvoicePositions(token, compName, custNr, invoiceNr, orderNr, page));
    return retryIfExpiredToken(function).getBody().getInvoicePositions().stream()
        .map(AxInvoicePosition::toPositionDto).collect(Collectors.toList());
  }

  @Override
  public Optional<String> getInvoicePdfUrl(final String compName, final String custNr,
      final String invoiceNr) {
    Function<String, ResponseEntity<AxInvoicePDF>> function = token -> getOrDefaultThrow(
        () -> axClient.getInvoicesPdf(token, compName, custNr, invoiceNr));
    return Optional.ofNullable(retryIfExpiredToken(function).getBody().getUrl());
  }

  @Override
  public List<Courier> getCouriers(final String companyName) {
    Function<String, ResponseEntity<AxCouriersResourceSupport>> function =
        token -> axClient.getCouriers(token, companyName);
    final ResponseEntity<AxCouriersResourceSupport> axBranches = retryIfExpiredToken(function);
    if (!axBranches.hasBody() || !axBranches.getBody().hasCourierss()) {
      return Collections.emptyList();
    }
    return axBranches.getBody().getCourierServices().stream().map(AxCourier::toCourierDto)
        .collect(Collectors.toList());
  }

}
