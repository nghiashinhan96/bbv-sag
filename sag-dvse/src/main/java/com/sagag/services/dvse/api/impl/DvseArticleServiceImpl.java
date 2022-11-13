package com.sagag.services.dvse.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.dvse.api.DvseArticleService;
import com.sagag.services.dvse.builder.DvseArticleItemBuilder;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.profiles.DefaultDvseProfile;
import com.sagag.services.dvse.utils.DvseUtils;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfItem;
import com.sagag.services.dvse.wsdl.dvse.GetBackItems;
import com.sagag.services.dvse.wsdl.dvse.Item;
import com.sagag.services.dvse.wsdl.dvse.ResponseLimit;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.DeliveryProfileCacheService;
import com.sagag.services.hazelcast.api.ExternalVendorCacheService;
import com.sagag.services.hazelcast.api.NextWorkingDateCacheService;
import com.sagag.services.hazelcast.api.TourTimeCacheService;


/**
 * The service for getting article information from SAG Connect API.
 *
 */
@Service
@DefaultDvseProfile
public class DvseArticleServiceImpl extends DvseProcessor implements DvseArticleService {

  @Autowired
  private ArticleSearchExternalExecutor axArticleSearchExecutor;

  @Autowired
  private NextWorkingDateCacheService nextWorkingDateCacheService;

  @Autowired
  private ContextService contextService;

  @Autowired
  private DvseArticleItemBuilder dvseArticleItemBuilder;

  @Autowired
  private ExternalVendorCacheService externalVendorCacheService;

  @Autowired
  private DeliveryProfileCacheService deliveryProfileCacheService;

  @Autowired
  private TourTimeCacheService tourTimeCacheService;

  @Override
  public GetBackItems getArticleInformation(final ConnectUser user, final ArrayOfItem items) {

    // Step 1: Map item to article
    final List<Item> itemList = items.getItem();
    final Map<Item, ArticleDocDto> itemsData = DvseUtils.convertItemsToArticles(itemList);

    // Step 2: update article information from ERP
    final SupportedAffiliate affiliate = user.getAffiliate();
    final Optional<VehicleDto> vehicle = searchVehicle(DvseUtils.getKtypeNr(itemList));
    final List<ArticleDocDto> articles = searchArticles(affiliate,
      DvseUtils.getPimIdAndQuantities(itemList), user.isSaleOnBehalf());
    final Customer customer = user.getCustomer();

    final ErpSendMethodEnum sendMethodEnum =
        contextService.getEshopContext(user.key()).getDeliveryType();
    final String pickupBranchId = contextService.getPickupBranchId(user);
    final NextWorkingDates nextWorkingDate = nextWorkingDateCacheService.get(user, pickupBranchId);
    ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .affiliate(affiliate)
        .custNr(user.getCustNrStr())
        .companyName(user.getCompanyName())
        .availabilityUrl(customer.getAvailabilityRelativeUrl())
        .filterArticleBefore(true)
        .updatePrice(true)
        .updateAvailability(true)
        .isCartMode(false)
        .vehicle(vehicle.orElse(null))
        .addressId(user.getDeliveryAddressId())
        .pickupBranchId(pickupBranchId)
        .defaultBrandId(user.getDefaultBranchId())
        .deliveryType(sendMethodEnum.name())
        .articles(articles)
        .nextWorkingDate(nextWorkingDate)
        .externalVendors(externalVendorCacheService.findAll())
        .deliveryProfiles(deliveryProfileCacheService.findAll())
        .customerTourTimes(tourTimeCacheService.searchTourTimesByCustNr(user.getCustNr()))
        .priceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum())
        .isDvseMode(true)
        .build();

    final List<ArticleDocDto> updatedArticles = axArticleSearchExecutor.execute(criteria);

    // Step 3: Build GetBackItems
    return buildGetBackItems(affiliate, sendMethodEnum, itemsData, updatedArticles,
        nextWorkingDate);
  }

  private GetBackItems buildGetBackItems(final SupportedAffiliate affiliate,
      final ErpSendMethodEnum sendMethod, final Map<Item, ArticleDocDto> itemsData,
      List<ArticleDocDto> updatedArticles, final NextWorkingDates nextWorkingDateTime) {

    final List<Item> items = new ArrayList<>();
    itemsData.forEach((item, data) -> {
      final Optional<ArticleDocDto> updatedArticle = updatedArticles.stream()
          .filter(filterByWholesalerArticleNumber(item.getWholesalerArticleNumber())).findFirst();

      items.add(dvseArticleItemBuilder.buildArticeItem(item, updatedArticle,
          affiliate, sendMethod, nextWorkingDateTime));
    });

    final GetBackItems result = new GetBackItems();

    final ArrayOfItem resultItems = new ArrayOfItem();
    resultItems.getItem().addAll(items);
    result.setItems(resultItems);

    // dvse need response limit to be present, even though it's always the same.
    ResponseLimit responseLimit = new ResponseLimit();
    responseLimit.setIsResponseBounded(false);
    responseLimit.setResponseMax(null);
    result.setResponseLimit(responseLimit);

    return result;
  }

  private static Predicate<ArticleDocDto> filterByWholesalerArticleNumber(
      final String wholesalerArticleNumber) {
    return article -> StringUtils.equals(wholesalerArticleNumber, article.getIdSagsys());
  }

}
