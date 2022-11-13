package com.sagag.services.ax.availability.filter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.enums.LocationType;
import com.sagag.services.common.enums.WtLocationSplitting;
import com.sagag.services.common.enums.WtLocationState;
import com.sagag.services.common.profiles.WintProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.LocationAvailabilityItem;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.GrantedBranch;

import lombok.extern.slf4j.Slf4j;

@WintProfile
@Component
@Slf4j
public class WtAvailabilityBuilder {

  public static final int LOCAL_FIRST_PRIORITY = 1;

  public String buildAvailState(ArticleDocDto article, List<GrantedBranch> grantedBranches) {
    if (hasEnoughWtStock(article) && hasStockInGrantedBranches(article, grantedBranches)) {
      return WtLocationState.GREEN.name();
    }
    if (hasWtStock(article)) {
      return WtLocationState.YELLOW.name();
    }
    return WtLocationState.RED.name();
  }

  public String buildAvailName(ArticleDocDto article, List<GrantedBranch> grantedBranches) {
    GrantedBranch prioBranch = getPrioBranch(grantedBranches);
    if (article.getAmountNumber() <= findStockInBranch(article, prioBranch)) {
      return WtLocationSplitting.NONE.name();
    }
    if (hasWtStock(article) && hasStockInGrantedBranches(article, grantedBranches)) {
      return WtLocationSplitting.MULTI_SHOP.name();
    }
    if (hasWtStock(article)) {
      return WtLocationSplitting.PARTLY_LOCAL.name();
    }
    return WtLocationSplitting.NOT_AVAILABLE.name();
  }

  public boolean isAllInGrantedBranches(List<LocationAvailabilityItem> localAvailItems,
      List<String> sortedgrantedBranchIds, Integer requestedAmount) {

    Stream<LocationAvailabilityItem> locals = CollectionUtils.emptyIfNull(localAvailItems).stream()
        .filter(loc -> sortedgrantedBranchIds.contains(loc.getLocationId()));

    return locals.collect(
        Collectors.summingDouble(LocationAvailabilityItem::getQuantity)) >= requestedAmount;
  }

  public List<LocationAvailabilityItem> buildLocalAvailItems(ArticleDocDto article,
      List<GrantedBranch> sortedgrantedBranches, List<CustomerBranch> companyBranches) {

    List<ArticleStock> stocks = article.getWtStocks();
    if (CollectionUtils.isEmpty(stocks)) {
      return Collections.emptyList();
    }

    return sortLocation(article, sortedgrantedBranches, companyBranches);
  }

  public static boolean hasWtStock(ArticleDocDto article) {
    return sumStocks(article.getWtStocks()) > 0d;
  }

  private static boolean hasStockInGrantedBranches(ArticleDocDto article,
      List<GrantedBranch> grantedBranches) {
    List<String> grantedBranchIds =
        grantedBranches.stream().map(GrantedBranch::getBranchId).collect(Collectors.toList());
    List<ArticleStock> wtStocksOfGrantedBranches =
        CollectionUtils.emptyIfNull(article.getWtStocks()).stream()
        .filter(stock -> grantedBranchIds.contains(stock.getBranchId()))
        .collect(Collectors.toList());

    return sumStocks(wtStocksOfGrantedBranches) > 0d;
  }

  private static boolean hasEnoughWtStock(ArticleDocDto article) {
    return sumStocks(article.getWtStocks()) >= article.getAmountNumber();
  }

  private static double sumStocks(List<ArticleStock> wtStocks) {

    return CollectionUtils.emptyIfNull(wtStocks).stream()
        .collect(Collectors.summingDouble(ArticleStock::getStock));
  }

  private GrantedBranch getPrioBranch(List<GrantedBranch> grantedBranches) {
    return CollectionUtils.emptyIfNull(grantedBranches).stream()
        .filter(branch -> branch != null && branch.getOrderingPriority() == LOCAL_FIRST_PRIORITY)
        .findFirst().orElseThrow(() -> new IllegalArgumentException("Not found prior branch!"));
  }

  private double findStockInBranch(ArticleDocDto article, GrantedBranch prioBranch) {
    Optional<ArticleStock> stockOpt = CollectionUtils.emptyIfNull(article.getWtStocks()).stream()
        .filter(stock -> stock.getBranchId().equals(prioBranch.getBranchId())).findFirst();
    if (!stockOpt.isPresent()) {
      return 0d;
    }
    return stockOpt.get().getStock();
  }

  protected Comparator<GrantedBranch> prioBranchComparator() {
    return (branch1, branch2) -> Integer.compare(branch1.getOrderingPriority(),
        branch2.getOrderingPriority());
  }

  private List<LocationAvailabilityItem> sortLocation(ArticleDocDto article,
      List<GrantedBranch> sortedgrantedBranches, List<CustomerBranch> companyBranches) {
    log.debug("sortLocation sortedgrantedBranches: {}", sortedgrantedBranches);
    Map<String, CustomerBranch> companyBranchesMap = CollectionUtils.emptyIfNull(companyBranches).stream()
        .collect(Collectors.toMap(CustomerBranch::getBranchId, b -> b));

    Double[] requestedAmount = { (double) article.getAmountNumber() };
    Map<String, ArticleStock> wtStocksMap = CollectionUtils.emptyIfNull(article.getWtStocks())
        .stream().collect(Collectors.toMap(ArticleStock::getBranchId, st -> st));

    log.debug("sortLocation wtStocksMap: {}", wtStocksMap);
    log.debug("sortLocation requestedAmount: {}", requestedAmount[0]);
    List<LocationAvailabilityItem> locationAvailabilityItems = new ArrayList<>();
    locationAvailabilityItems.addAll(buildPrioLocalStocks(sortedgrantedBranches, requestedAmount,
        wtStocksMap, companyBranchesMap));
    locationAvailabilityItems
        .addAll(buildNonPrioLocalStocks(requestedAmount, wtStocksMap, companyBranchesMap));
    fillLocationType(locationAvailabilityItems, sortedgrantedBranches);

    return locationAvailabilityItems;
  }

  private List<LocationAvailabilityItem> buildPrioLocalStocks(
      List<GrantedBranch> sortedgrantedBranches, Double[] requestedAmount,
      Map<String, ArticleStock> wtStocksMap, Map<String, CustomerBranch> companyBranchesMap) {

    List<LocationAvailabilityItem> locationAvailabilityItems = new ArrayList<>();
    for (GrantedBranch branch : sortedgrantedBranches) {
      if (requestedAmount[0] == 0) {
        break;
      }

      String branchId = branch.getBranchId();
      ArticleStock articleStock = wtStocksMap.get(branchId);
      if (Objects.nonNull(articleStock)) {
        LocationAvailabilityItem selectedLocation =
            buildLocationAvailabilityItem(companyBranchesMap, branchId);
        resetPhoneNumberForPrioBranch(selectedLocation);
        double selectedStock = requestedAmount[0] <= articleStock.getStock() ? requestedAmount[0]
            : articleStock.getStock();
        selectedLocation.setQuantity(selectedStock);
        locationAvailabilityItems.add(selectedLocation);
        requestedAmount[0] = requestedAmount[0] - selectedStock;
        wtStocksMap.remove(branchId);
      }
    }

    return locationAvailabilityItems;
  }

  private void resetPhoneNumberForPrioBranch(LocationAvailabilityItem selectedLocation) {
    selectedLocation.setLocationPhoneNr(StringUtils.EMPTY);
  }

  private void fillLocationType(List<LocationAvailabilityItem> selectedLocations,
      List<GrantedBranch> sortedgrantedBranches) {
    selectedLocations.forEach(selectedLocation -> {
      Optional<GrantedBranch> grantedBranchOpt = sortedgrantedBranches.stream()
          .filter(branch -> branch.getBranchId().equals(selectedLocation.getLocationId()))
          .findFirst();
      Integer orderingPriority =
          grantedBranchOpt.isPresent() ? grantedBranchOpt.get().getOrderingPriority() : null;
      selectedLocation.setLocationType(LocationType.fromOrderingPriority(orderingPriority).name());
    });
  }

  private List<LocationAvailabilityItem> buildNonPrioLocalStocks(Double[] requestedAmount,
      Map<String, ArticleStock> wtStocksMap, Map<String, CustomerBranch> companyBranchesMap) {
    List<LocationAvailabilityItem> locationAvailabilityItems = new ArrayList<>();
    if (requestedAmount[0] > 0) {
      wtStocksMap.values().forEach(st -> {
        LocationAvailabilityItem selectedLocation =
            buildLocationAvailabilityItem(companyBranchesMap, st.getBranchId());
        selectedLocation.setQuantity(st.getStock());
        locationAvailabilityItems.add(selectedLocation);
      });
    }
    return locationAvailabilityItems;
  }

  private LocationAvailabilityItem buildLocationAvailabilityItem(
      Map<String, CustomerBranch> companyBranchesMap, String branchId) {
    LocationAvailabilityItem selectedLocation = new LocationAvailabilityItem();
    CustomerBranch customerBranch = companyBranchesMap.get(branchId);
    String branchName =
        Objects.nonNull(customerBranch) ? customerBranch.getBranchName() : StringUtils.EMPTY;
    String branchPhone =
        Objects.nonNull(customerBranch) ? customerBranch.getPhoneNr() : StringUtils.EMPTY;
    selectedLocation.setLocationId(branchId);
    selectedLocation.setLocationName(branchName);
    selectedLocation.setLocationPhoneNr(branchPhone);

    return selectedLocation;
  }

}
