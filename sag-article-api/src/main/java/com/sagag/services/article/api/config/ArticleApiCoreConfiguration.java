package com.sagag.services.article.api.config;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.BranchExternalService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.article.api.InvoiceExternalService;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.article.api.ReturnExternalOrderService;
import com.sagag.services.article.api.SoapArticleErpExternalService;
import com.sagag.services.article.api.SoapSendOrderExternalService;
import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.article.api.availability.AvailabilitiesFilterContext;
import com.sagag.services.article.api.availability.AvailabilityFilter;
import com.sagag.services.article.api.availability.IArticleAvailabilityProcessor;
import com.sagag.services.article.api.availability.MultipleGroupArticleAvailabilitiesSplitter;
import com.sagag.services.article.api.availability.StockAvailabilitiesFilter;
import com.sagag.services.article.api.availability.comparator.AvailabilityArticleComparator;
import com.sagag.services.article.api.availability.externalvendor.ExternalStockFinder;
import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.article.api.executor.DefaultArticleSearchExternalExecutor;
import com.sagag.services.article.api.executor.DisplayedPriceSearchExecutor;
import com.sagag.services.article.api.price.PriceDiscountPromotionCalculator;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.article.api.request.ExternalOrderHistoryRequest;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.article.api.request.InvoiceExternalSearchRequest;
import com.sagag.services.article.api.request.OrderStatusRequest;
import com.sagag.services.article.api.request.PriceRequest;
import com.sagag.services.article.api.request.returnorder.ReturnedOrderRequestBody;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.domain.sag.erp.ExternalOrderHistory;
import com.sagag.services.domain.sag.erp.ExternalOrderPositions;
import com.sagag.services.domain.sag.erp.OrderConfirmation;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.Employee;
import com.sagag.services.domain.sag.invoice.InvoiceDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderBatchJobsResultDto;
import com.sagag.services.domain.sag.returnorder.ReturnOrderDto;
import com.sagag.services.domain.sag.returnorder.TransactionReferenceDto;

import org.apache.commons.collections4.ListUtils;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

;

@Configuration
public class ArticleApiCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(AdditionalCriteriaBuilder.class)
  public AdditionalCriteriaBuilder defaultAdditionalCriteriaBuilder() {
    return (searchString, kType, isDetailRequest, finalCustomerId) -> Optional.empty();
  }

  private static UnsupportedOperationException buildUnsupportedApiEx(String apiName) {
    return new UnsupportedOperationException(
        String.format("No support method (%s) from ERP services", apiName));
  }

  @Bean
  @ConditionalOnMissingBean(IArticleAvailabilityProcessor.class)
  public IArticleAvailabilityProcessor defaultArticleAvailabilityProcessor() {
    return new IArticleAvailabilityProcessor() {

      @Override
      public ArticleAvailabilityResult process(Availability availability,
          ErpSendMethodEnum sendMethod, DateTime nextWorkingTour) {
        throw buildUnsupportedApiEx("process");
      }

      @Override
      public ArticleAvailabilityResult getDefaultResult() {
        throw buildUnsupportedApiEx("getDefaultResult");
      }

      @Override
      public ArticleAvailabilityResult getDefaultResultForNoPrice() {
        throw buildUnsupportedApiEx("getDefaultResultForNoPrice");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(AvailabilityFilter.class)
  public AvailabilityFilter defaultAvailabilityFilter() {
    return (article, artCriteria, tourTimeList, openingHours, countryName) -> article == null
        ? Lists.newArrayList() : Lists.newArrayList(article.getAvailabilities());
  }

  @Bean
  @ConditionalOnMissingBean(StockAvailabilitiesFilter.class)
  public StockAvailabilitiesFilter defaultStockAvailabilitiesFilter() {
    return new StockAvailabilitiesFilter() {

      @Override
      public List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> articles,
          String countryName, ArticleSearchCriteria criteria,
          AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors) {
        throw buildUnsupportedApiEx("doFilterAvailabilities");
      }

      @Override
      public Predicate<ArticleDocDto> extractFilter() {
        throw buildUnsupportedApiEx("extractFilter");
      }

      @Override
      public AvailabilityFilter availabilityFilter() {
        throw buildUnsupportedApiEx("availabilityFilter");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(ReturnExternalOrderService.class)
  public ReturnExternalOrderService defaultReturnExternalOrderService() {
    return new ReturnExternalOrderService() {

      @Override
      public List<TransactionReferenceDto> searchTransactionReferences(String compName,
          String reference) {
        throw buildUnsupportedApiEx("searchTransactionReferences");
      }

      @Override
      public ReturnOrderDto createReturnOrder(String companyName,
          ReturnedOrderRequestBody request) {
        throw buildUnsupportedApiEx("createReturnOrder");
      }

      @Override
      public Optional<ReturnOrderBatchJobsDto> searchReturnOrderBatchJobStatus(String compName,
          List<String> batchJobIds) {
        throw buildUnsupportedApiEx("searchReturnOrderBatchJobStatus");
      }

      @Override
      public Optional<ReturnOrderBatchJobsResultDto> searchReturnOrderNumber(String compName,
          List<String> journalIds) {
        throw buildUnsupportedApiEx("searchReturnOrderNumber");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(OrderExternalService.class)
  public OrderExternalService defaultOrderExternalService() {
    return new OrderExternalService() {

      @Override
      public OrderConfirmation createOrder(String companyName, ExternalOrderRequest request) {
        throw buildUnsupportedApiEx("createOrder");
      }

      @Override
      public OrderConfirmation createBasket(String companyName, ExternalOrderRequest request) {
        throw buildUnsupportedApiEx("createBasket");
      }

      @Override
      public OrderConfirmation createOffer(String companyName, ExternalOrderRequest request) {
        throw buildUnsupportedApiEx("createOffer");
      }

      @Override
      public Optional<ExternalOrderHistory> getExternalOrderByHrefLink(String relativeUrl) {
        throw buildUnsupportedApiEx("getExternalOrderByHrefLink");
      }

      @Override
      public Optional<ExternalOrderHistory> getExternalOrderHistoryOfCustomer(String companyName,
          String customerNr, ExternalOrderHistoryRequest request, Integer page) {
        throw buildUnsupportedApiEx("getExternalOrderHistoryOfCustomer");
      }

      @Override
      public Optional<ExternalOrderPositions> getOrderPositions(String companyName,
          String customerNr, String orderNr) {
        throw buildUnsupportedApiEx("getOrderPositions");
      }

      @Override
      public boolean updateSalesOrderStatus(String compName, OrderStatusRequest request) {
        throw buildUnsupportedApiEx("updateSalesOrderStatus");
      }

      @Override
      public Optional<ExternalOrderHistory> getOrderDetailByOrderNr(String compName, String custNr,
          String orderNr) {
        throw buildUnsupportedApiEx("getOrderDetailByOrderNr");
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(InvoiceExternalService.class)
  public InvoiceExternalService defaultInvoiceExternalService() {
    return new InvoiceExternalService() {

      @Override
      public List<InvoiceDto> searchInvoices(String compName, String custNr,
          InvoiceExternalSearchRequest request) {
        throw buildUnsupportedApiEx("searchInvoices");
      }

      @Override
      public Optional<InvoiceDto> getInvoiceDetail(String compName, String custNr, String invoiceNr,
          String orderNr, Optional<Address> invoiceAddress) {
        throw buildUnsupportedApiEx("getInvoiceDetail");
      }

      @Override
      public Optional<String> getInvoicePdfUrl(String compName, String custNr, String invoiceNr) {
        throw buildUnsupportedApiEx("getInvoicePdfUrl");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(EmployeeExternalService.class)
  public EmployeeExternalService defaultEmployeeExternalService() {
    return new EmployeeExternalService() {

      @Override
      public Optional<Employee> findEmployee(String companyName, String emailAddress) {
        throw buildUnsupportedApiEx("findEmployee");
      }

      @Override
      public PaymentMethodType getConnectPaymentForSales(String axPaymentType, boolean isValidKSL) {
        throw buildUnsupportedApiEx("getConnectPaymentForSales");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(CustomerExternalService.class)
  public CustomerExternalService defaultCustomerExternalService() {
    return new CustomerExternalService() {

      @Override
      public Optional<Customer> findCustomerByNumber(String companyName, String customerNr) {
        throw buildUnsupportedApiEx("findCustomerByNumber");
      }

      @Override
      public Optional<Address> findCustomerAddressById(String company, String customerNr,
          String addressId) {
        throw buildUnsupportedApiEx("findCustomerAddressById");
      }

      @Override
      public Optional<CustomerBranch> searchBranchById(String companyName, String branchId) {
        throw buildUnsupportedApiEx("searchBranchById");
      }

      @Override
      public List<CustomerBranch> getCustomerBranches(String companyName, String defaultBranchId,
          boolean isSaleOnBehalf) {
        return Collections.emptyList();
      }

      @Override
      public Optional<CreditLimitInfo> getCreditLimitInfoByCustomer(String compName,
          String custNr) {
        throw buildUnsupportedApiEx("getCreditLimitInfoByCustomer");
      }

      @Override
      public Optional<CustomerInfo> getActiveCustomerInfo(String compName, String custNr) {
        throw buildUnsupportedApiEx("getActiveCustomerInfo");
      }

      @Override
      public List<Address> searchCustomerAddresses(String companyName, String customerNr) {
        throw buildUnsupportedApiEx("searchCustomerAddresses");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(ArticleExternalService.class)
  public ArticleExternalService defaultArticleExternalService() {
    return new ArticleExternalService() {

      @Override
      public Map<String, BulkArticleResult> searchByArticleIds(String compName,
          List<String> articleIds) {
        throw buildUnsupportedApiEx("searchByArticleIds");
      }

      @Override
      public Map<String, BulkArticleResult> searchByUmarIds(String compName, String umarIds) {
        throw buildUnsupportedApiEx("searchByUmarIds");
      }

      @Override
      public Map<String, PriceWithArticle> searchPrices(String companyName, PriceRequest request,
          boolean isFinalCustomerUser) {
        throw buildUnsupportedApiEx("searchPrices");
      }

      @Override
      public Map<String, List<ArticleStock>> searchStocks(String compName, List<String> articleIds,
          String branchId) {
        throw buildUnsupportedApiEx("searchStocks");
      }

      @Override
      public Map<String, List<Availability>> searchAvailabilities(AvailabilityRequest req) {
        throw buildUnsupportedApiEx("searchAvailabilities");
      }

      @Override
      public Optional<Date> getNextWorkingDate(String companyName, String branchId,
          Date requestDate) {
        throw buildUnsupportedApiEx("getNextWorkingDate");
      }

      @Override
      public List<VendorDto> searchVendors(String companyName, List<String> articleIds) {
        throw buildUnsupportedApiEx("searchVendors");
      }

      @Override
      public Optional<VendorStockDto> searchVendorStock(String companyName, String vendorId,
          String branchId, List<BasketPosition> positions) {
        throw buildUnsupportedApiEx("searchVendorStock");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(SoapArticleErpExternalService.class)
  public SoapArticleErpExternalService defaultSoapArticleErpExternalService() {
    return new SoapArticleErpExternalService() {

      @Override
      public List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username,
          String customerId, String securityToken, String language, List<ArticleDocDto> articles,
          double vatRate, AdditionalSearchCriteria additional) {
        return Collections.emptyList();
      }

      @Override
      public List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username,
          String customerId, String securityToken, String language, List<ArticleDocDto> articles,
          double vatRate, Optional<VehicleDto> vehicleOpt) {
        return Collections.emptyList();
      }

      @Override
      public List<ArticleDocDto> searchArticleAvailabilitiesDetails(String username,
          String customerId, String securityToken, String language, List<ArticleDocDto> articles,
          double vatRate, Optional<VehicleDto> vehicleOpt) {
        return Collections.emptyList();
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(SoapSendOrderExternalService.class)
  public SoapSendOrderExternalService defaultSoapSendOrderExternalService() {
    return new SoapSendOrderExternalService() {

      @Override
      public OrderConfirmation sendOrder(String username, String customerId, String securityToken,
          String language, ExternalOrderRequest request, AdditionalSearchCriteria additional)
          throws ServiceException {
        throw buildUnsupportedApiEx("sendOrder");
      }

      @Override
      public Optional<ExternalOrderHistory> getExternalOrderHistoryOfCustomer(String companyName,
          String customerNr, ExternalOrderHistoryRequest request, Integer page) {
        throw buildUnsupportedApiEx("getExternalOrderHistoryOfCustomer");
      }

      @Override
      public Optional<ExternalOrderPositions> getExternalOrderPosistion(String orderNr,
          String companyName, String customerNr) {
        throw buildUnsupportedApiEx("getExternalOrderPosistion");
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(ExternalStockFinder.class)
  public ExternalStockFinder defaultExternalStockFinder() {
    return (articles, criteria) -> Collections.emptyList();
  }

  @Bean
  @ConditionalOnMissingBean(DisplayedPriceSearchExecutor.class)
  public DisplayedPriceSearchExecutor emptyDisplayedPriceSearchExecutor() {
    return request -> Lists.newArrayList();
  }

  @Bean
  @ConditionalOnMissingBean(MultipleGroupArticleAvailabilitiesSplitter.class)
  public MultipleGroupArticleAvailabilitiesSplitter defMultipleGroupArtAvailabilitiesSplitter() {
    return articles -> { /** do nothing */ };
  }

  @Bean
  @ConditionalOnMissingBean(AvailabilityArticleComparator.class)
  public AvailabilityArticleComparator defaultAvailabilityArticleComparator() {
    return (art1, art2) -> 0;
  }

  @Bean
  @ConditionalOnMissingBean(ArticleSearchExternalExecutor.class)
  public ArticleSearchExternalExecutor defArticleSearchExternalExecutor() {
    return new DefaultArticleSearchExternalExecutor();
  }

  @Bean
  @ConditionalOnMissingBean(AvailabilitiesFilterContext.class)
  public AvailabilitiesFilterContext defAvailabilitiesFilterContext() {
    return (articles, criteria) -> Lists.newArrayList(ListUtils.emptyIfNull(articles));
  }

  @Bean
  @ConditionalOnMissingBean(PriceDiscountPromotionCalculator.class)
  public PriceDiscountPromotionCalculator defPriceDiscountPromotionCalculator() {
    return (priceWithArticle, quantity) -> { /** do nothing */ };
  }

  @Bean
  @ConditionalOnMissingBean(BranchExternalService.class)
  public BranchExternalService defBranchExtService() {
    return companyName -> Collections.emptyList();
  }
}
