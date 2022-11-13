package com.sagag.services.ax.availability.externalvendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.request.BasketPosition;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.ax.domain.vendor.VendorDeliveryInfo;
import com.sagag.services.ax.domain.vendor.VendorDeliveryInfo.VendorDeliveryInfoBuilder;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VenExternalVendorAvailabilityFilter extends AbstractExternalVendorAvailabilityFilter
    implements ExternalVendorStockFinder {

  private static final int DEFAULT_QUANTITY_IN_CASE_OF_NULL_QUANTITY = 0;

  @Autowired
  private ArticleExternalService articleExternalService;

  @Autowired
  private List<VenAvailabilityCalculator> venAvailabilityCalculators;

  @Override
  public List<AxAvailabilityType> availabilityTypes() {
    return Arrays.asList(AxAvailabilityType.VEN, AxAvailabilityType.VWO, AxAvailabilityType.VWH,
        AxAvailabilityType.VWI);
  }

  @Override
  public List<ArticleDocDto> filter(List<ArticleDocDto> articles, ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors,
      String countryName) {

    if (CollectionUtils.isEmpty(axVendors)) {
      return Lists.newArrayList();
    }

    List<ExternalStockInfo> stocksInfo = findExternalStocks(artCriteria, axVendors, articles);

    if (CollectionUtils.isEmpty(stocksInfo)) {
      return Lists.newArrayList();
    }

    final List<VendorStockDto> foundVendorStocks =
        searchVendorStocksByAxVendor(artCriteria.getCompanyName(), stocksInfo, artCriteria);
    log.info("[BBV] findExternalStocks: searchVendorStocks - done");

    if (CollectionUtils.isEmpty(foundVendorStocks)) {
      return Collections.emptyList();
    }
    List<ExternalStockInfo> stockWithVendorInfo =
        extractVendorDeliveryInfo(stocksInfo, foundVendorStocks);

    final Map<ArticleDocDto, List<ExternalStockInfo>> extStockInfoByArticle =
        CollectionUtils.emptyIfNull(stockWithVendorInfo).stream().filter(ExternalStockInfo::isValid)
            .sorted(ExternalStockInfoUtil.sortByPriorityComparator())
            .collect(Collectors.groupingBy(ExternalStockInfo::getArticle));

    List<ExternalStockInfo> validExtStockInfo = prioritizeExternalStock(extStockInfoByArticle);

    CollectionUtils.emptyIfNull(validExtStockInfo).forEach(stock -> {
      VenAvailabilityCalculator venAvailabilityCalculator = venAvailabilityCalculators.stream()
          .filter(calculator -> calculator.isSupportedType(
              Optional.ofNullable(stock.getAvailabilityTypeid()).orElse(StringUtils.EMPTY)))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("AvailabilityTypeId should not be null"));
      venAvailabilityCalculator.calculateAvailability(artCriteria, availCriteria, countryName,
          stock);
    });

    return validExtStockInfo.stream().map(ExternalStockInfo::getArticle)
        .collect(Collectors.toList());
  }

  @Override
  public List<ExternalStockInfo> findExternalStocks(ArticleSearchCriteria artCriteria,
      List<VendorDto> axVendors, List<ArticleDocDto> articles) {
    log.info("[BBV] findExternalStocks: start");
    final List<ExternalVendorDto> allExternalVendor = artCriteria.getExternalVendors();
    final List<DeliveryProfileDto> allDeliveryProfile = artCriteria.getDeliveryProfiles();
    final String deliveryType = artCriteria.getDeliveryType();
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.valueOf(deliveryType);
    final List<String> branchIds = AxBranchUtils.getBranchIdsByDeliveryType(sendMethod,
        artCriteria.getPickupBranchId(), artCriteria.findCustomerBranchIds());

    final List<ExternalVendorDto> venExternalVendors =
        selectExternalVendorsByType(allExternalVendor);

    final List<ExternalStockInfo> stockInfos =
        getStockInfos(venExternalVendors, axVendors, articles, allDeliveryProfile, branchIds);
    log.info("[BBV] findExternalStocks: getStockInfos - done");

    if (CollectionUtils.isEmpty(stockInfos)) {
      return Collections.emptyList();
    }

    log.info("[BBV] findExternalStocks: end");
    return stockInfos;
  }

  protected void updateDeliveryStockInfo(ExternalStockInfo item, final String idSagsys,
      VendorStockDto stock, boolean usePriorityFromDB) {
    if (stock == null) {
      return;
    }
    VendorDeliveryInfoBuilder vendorDeliveryInfoBuilder = VendorDeliveryInfo.builder();
    Optional.ofNullable(stock.getCutoffTime())
        .map(DateUtils::parseUTCDateFromStringThrowExceptionIfHasProblem)
        .ifPresent(vendorDeliveryInfoBuilder::vendorCutOffTime);
    Optional.ofNullable(stock.getDeliveryDate())
        .map(DateUtils::parseUTCDateFromStringThrowExceptionIfHasProblem)
        .ifPresent(vendorDeliveryInfoBuilder::vendorDeliveryTime);
    stock.getReturnedQuantity(idSagsys).ifPresent(vendorDeliveryInfoBuilder::returnedQuantity);
    Optional.ofNullable(stock.getBranchId()).ifPresent(vendorDeliveryInfoBuilder::branchId);

    item.addDeliveryProfile(vendorDeliveryInfoBuilder.build());
    if (!usePriorityFromDB) {
      item.updatePriority(stock.getEPriority());
    }
  }

  protected Optional<VendorStockDto> getVendorStock(String companyName, String vendorId,
      String branchId, List<ExternalStockInfo> stockInfos) {

    if (StringUtils.isBlank(branchId)) {
      return Optional.empty();
    }

    final List<BasketPosition> basketPositions =
        stockInfos.stream().map(buildBasketPositions()).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(basketPositions)) {
      return Optional.empty();
    }

    Optional<VendorStockDto> searchVendorStock =
        articleExternalService.searchVendorStock(companyName, vendorId, branchId, basketPositions);

    searchVendorStock.ifPresent(item -> {
      item.setBranchId(branchId);
      item.setVendorId(vendorId);
    });
    return searchVendorStock;
  }

  private Function<ExternalStockInfo, BasketPosition> buildBasketPositions() {
    return item -> {
      ArticleDocDto article = item.getArticle();
      VendorDto vendor = item.getVendor();
      String externalArticleId = vendor.getExternalArticleId();
      if (StringUtils.isBlank(externalArticleId)) {
        return null;
      }
      return buildBasketPosition(article.getIdSagsys(), externalArticleId,
          article.getAmountNumber());
    };
  }

  private BasketPosition buildBasketPosition(String articleId, String externalArticleId,
      Integer quantity) {
    BasketPosition basketPosition = new BasketPosition();
    basketPosition.setArticleId(articleId);
    basketPosition.setExternalArticleId(externalArticleId);
    basketPosition.setQuantity(quantity);
    return basketPosition;
  }

  private List<VendorStockDto> searchVendorStocksByAxVendor(String companyName,
      List<ExternalStockInfo> stocksInfo, ArticleSearchCriteria artCriteria) {

    final Map<String, List<ExternalStockInfo>> vendorMap =
        stocksInfo.stream().collect(Collectors.groupingBy(ExternalStockInfo::getVendorId));

    if (StringUtils.isBlank(companyName) || MapUtils.isEmpty(vendorMap)) {
      return new ArrayList<>();
    }
    List<VendorStockDto> vendorsStocks = new ArrayList<>();
    vendorMap.entrySet().forEach(item -> {
      List<String> customerBranchIds = artCriteria.findCustomerBranchIds();
      if (CollectionUtils.isEmpty(customerBranchIds)) {
        return;
      }
      customerBranchIds.forEach(branchId -> {
        String vendorId = item.getKey();
        getVendorStock(companyName, vendorId, branchId, item.getValue())
            .ifPresent(vendor -> vendorsStocks.add(vendor));
      });
    });

    return vendorsStocks;
  }

  private List<ExternalStockInfo> extractVendorDeliveryInfo(List<ExternalStockInfo> stockInfos,
      List<VendorStockDto> vendorStocks) {
    final boolean usePriorityFromDB = CollectionUtils.emptyIfNull(vendorStocks).stream().allMatch(
        vendorStock -> vendorStock.getEPriority() == ExternalStockInfo.MISSING_VENDOR_E_PRIORITY_);
    return stockInfos.stream().map(item -> {
      final String idSagsys = item.getArticle().getIdSagsys();
      CollectionUtils.emptyIfNull(findStockForArticle(idSagsys, vendorStocks, stockInfos))
          .forEach(stock -> updateDeliveryStockInfo(item, idSagsys, stock, usePriorityFromDB));
      return item;
    }).filter(ExternalStockInfo::isValidVenItem).collect(Collectors.toList());
  }

  private String fromArticleIdToExteralArticleId(String articleId,
      List<ExternalStockInfo> stockInfos) {
    return stockInfos.stream().filter(item -> articleId.equals(item.getArticle().getIdSagsys()))
        .map(ExternalStockInfo::getExternalArticleId).findAny().orElse(StringUtils.EMPTY);
  }

  private List<VendorStockDto> findStockForArticle(String articleId,
      List<VendorStockDto> vendorStocks, List<ExternalStockInfo> stockInfos) {
    return vendorStocks.stream()
        .filter(item -> item.containArticle(fromArticleIdToExteralArticleId(articleId, stockInfos)))
        .collect(Collectors.toList());
  }

  private List<ExternalStockInfo> prioritizeExternalStock(
      Map<ArticleDocDto, List<ExternalStockInfo>> extStockInfoByArticle) {
    List<ExternalStockInfo> result = new ArrayList<>();
    if (MapUtils.isNotEmpty(extStockInfoByArticle)) {
      extStockInfoByArticle.entrySet().forEach(extStock -> {
        List<ExternalStockInfo> stockInfos = extStock.getValue();
        if (CollectionUtils.isEmpty(stockInfos)) {
          return;
        }
        Map<ExternalStockInfo, Integer> maxQuantityMap = stockInfos.stream()
            .collect(Collectors.toMap(stock -> stock,
                stock -> stock.getVendorsDeliveryInfo().stream()
                    .map(VendorDeliveryInfo::getReturnedQuantity).max(Comparator.naturalOrder())
                    .orElse(DEFAULT_QUANTITY_IN_CASE_OF_NULL_QUANTITY)));
        if (MapUtils.isEmpty(maxQuantityMap)) {
          return;
        }
        maxQuantityMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue))
            .ifPresent(map -> result.add(map.getKey()));
      });
    }
    return result;
  }

}
