package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.executor.DisplayedPriceSearchExecutor;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceBrand;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequest;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceRequestItem;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceResponseItem;
import com.sagag.services.elasticsearch.api.ArticleVehiclesSearchService;
import com.sagag.services.elasticsearch.domain.article.ArticleVehicles;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleUsage;
import com.sagag.services.hazelcast.api.MakeCacheService;
import com.sagag.services.ivds.api.impl.ArticleProcessor;
import com.sagag.services.ivds.executor.IvdsDisplayedPricesArticleTaskExecutor;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class IvdsDisplayedPricesArticleTaskExecutorImpl extends ArticleProcessor
    implements IvdsDisplayedPricesArticleTaskExecutor {

  @Autowired
  private ArticleVehiclesSearchService articleVehiclesSearchService;

  @Autowired
  private MakeCacheService makeCacheService;

  @Autowired
  private DisplayedPriceSearchExecutor displayPriceSearchExecutor;

  @Override
  public List<DisplayedPriceResponseItem> executeTaskWithArticleDisplayPrices(UserInfo user,
      DisplayedPriceRequest request) {
    List<DisplayedPriceRequestItem> requestItems = request.getRequestItems();
    List<String> articleIds = requestItems.stream().map(DisplayedPriceRequestItem::getArticleId)
        .collect(Collectors.toList());
    final Page<ArticleDocDto> articles = searchArticlesByArtIdsAndExternalPartArtId(articleIds,
        user.isSaleOnBehalf());

    articles.stream().forEach(article -> {
      Integer amount =
          requestItems.stream().filter(item -> item.getArticleId().equals(article.getIdSagsys()))
              .findFirst().map(DisplayedPriceRequestItem::getAmount)
              .orElseThrow(() -> new IllegalArgumentException("Amount is required"));
      article.setAmountNumber(amount);
    });

    if (!articles.hasContent()) {
      return Collections.emptyList();
    }

    List<ArticleDocDto> articleList = articles.getContent();
    requestItems.stream().forEach(findArticleForRequestItem(articleList));
    List<DisplayedPriceRequestItem> validRequestItems = requestItems.stream()
        .filter(DisplayedPriceRequestItem::isValidItem).collect(Collectors.toList());

    List<DisplayedPriceRequestItem> itemWithBrands = validRequestItems.stream()
        .filter(DisplayedPriceRequestItem::hasVehicleBrand).collect(Collectors.toList());

    List<DisplayedPriceRequestItem> needTofindBrandArticleItems =
        validRequestItems.stream().filter(DisplayedPriceRequestItem::hasNoVehicleBrand)
            .map(findVehicleBrandsForArticle()).collect(Collectors.toList());

    request.setRequestItems(ListUtils.union(itemWithBrands, needTofindBrandArticleItems));
    return displayPriceSearchExecutor.execute(request);
  }

  private static Consumer<DisplayedPriceRequestItem> findArticleForRequestItem(
      List<ArticleDocDto> articleList) {
    return item -> articleList.stream()
        .filter(article -> article.getIdSagsys().equals(item.getArticleId())).findFirst()
        .ifPresent(item::setArticle);
  }

  private Function<DisplayedPriceRequestItem, DisplayedPriceRequestItem> findVehicleBrandsForArticle() {
    return requestItem -> {
      final List<ArticleVehicles> articleVehicles =
          articleVehiclesSearchService.searchArticleVehiclesByArtId(requestItem.getArticleId());

      List<String> vehicleBrands =
          articleVehicles.stream().flatMap(artVeh -> artVeh.getVehicles().stream())
              .map(VehicleUsage::getVehicleBrand).collect(Collectors.toList());
      Map<String, String> brandMap = makeCacheService.findMakeIdsByMakes(vehicleBrands);
      List<DisplayedPriceBrand> priceBrands = brandMap.entrySet().stream()
          .map(item -> new DisplayedPriceBrand(Long.valueOf(item.getKey()), item.getValue()))
          .collect(Collectors.toList());
      requestItem.setBrands(priceBrands);

      return requestItem;
    };
  }
}
