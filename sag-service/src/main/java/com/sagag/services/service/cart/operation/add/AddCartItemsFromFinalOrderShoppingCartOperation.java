package com.sagag.services.service.cart.operation.add;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.FinalCustomerOrderService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderDto;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderItemDto;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.service.cart.operation.DefaultUpdateInfoShoppingCartOperation;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class AddCartItemsFromFinalOrderShoppingCartOperation
    extends AbstractAddCartItemShoppingCartOperation<Long> {

  @Autowired
  private FinalCustomerOrderService finalCustomerOrderService;

  @Autowired
  private DefaultAddCartItemShoppingCartOperation addCartItemShopCartOperation;

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Autowired
  private DefaultUpdateInfoShoppingCartOperation updateInfoShoppingCartOperation;

  @Autowired
  private ArticleConverter articleConverter;

  @Override
  @LogExecutionTime
  public ShoppingCart execute(UserInfo user, Long finalCustomerOrderId, ShopType shopType,
      Object... additionals) {
    final String basketItemSourceId =  (String) additionals[0];
    final String basketItemSourceDesc =  (String) additionals[1];
    final Map<String, List<FinalCustomerOrderItemDto>> finalCustomerItemsByVehicleMap =
        new HashMap<>();
    Optional<FinalCustomerOrderDto> finalCustomerOrderDetailOpt =
        finalCustomerOrderService.getFinalCustomerOrderDetail(finalCustomerOrderId);

    List<FinalCustomerOrderItemDto> finalCustomerOrderItems = new ArrayList<>();
    finalCustomerOrderDetailOpt.ifPresent(
        finalCustomerOrder -> finalCustomerOrderItems.addAll(finalCustomerOrder.getItems()));

    finalCustomerOrderItems.forEach(item -> finalCustomerItemsByVehicleMap
        .compute(item.getVehicleId(), finalCustomerOrderCompute(item)));

    final Map<String, VehicleDto> vehicles = ivdsVehicleService.searchVehicles(
        finalCustomerItemsByVehicleMap.keySet().stream()
        .filter(vehId -> !StringUtils.isBlank(vehId)
            && !StringUtils.equals(vehId, SagConstants.KEY_NO_VEHICLE))
        .toArray(String[]::new));

    final Map<VehicleDto, List<ArticleDocDto>> articlesByVehicleMap =
        getUpdatedArticlesByVehicleMap(user, finalCustomerItemsByVehicleMap, vehicles);

    // Clear current sub shopping cart
    if (ShopType.SUB_FINAL_SHOPPING_CART == shopType) {
      cartManager().clearCart(user.getCachedUserId(), user.getCustNrStr(), shopType);
      finalCustomerOrderService.changeOrderStatusToOpen(finalCustomerOrderId);
    }

    final Long finalCustomerOrgId =
        finalCustomerOrderDetailOpt.map(FinalCustomerOrderDto::getOrgId).orElse(null);

    ShoppingCart shoppingCart = addCartItemShopCartOperation.execute(user,
        buildCartRequests(articlesByVehicleMap, finalCustomerOrgId,
                basketItemSourceId, basketItemSourceDesc), shopType);

    updateInfoShoppingCartOperation.execute(user, shoppingCart, shopType, StringUtils.EMPTY,
        StringUtils.EMPTY, NO_UPDATE_AVAIL_REQUEST);
    couponBusService.validateCoupon(user, shoppingCart);

    return shoppingCart;
  }


  private BiFunction<String, List<FinalCustomerOrderItemDto>, List<FinalCustomerOrderItemDto>>
  finalCustomerOrderCompute(
      FinalCustomerOrderItemDto item) {
    return (vehId, articleIds) -> {
      if (articleIds == null) {
        articleIds = new ArrayList<>();
      }
      articleIds.add(item);
      return articleIds;
    };
  }

  private Map<VehicleDto, List<ArticleDocDto>> getUpdatedArticlesByVehicleMap(final UserInfo user,
      final Map<String, List<FinalCustomerOrderItemDto>> finalCustomerItemsByVehicleMap,
      final Map<String, VehicleDto> vehicles) {


    final List<FinalCustomerOrderItemDto> finalCustomerOrderItems = finalCustomerItemsByVehicleMap
        .values().stream().flatMap(List::stream).collect(Collectors.toList());
    final List<String> allArticleIds = finalCustomerOrderItems.stream()
        .map(FinalCustomerOrderItemDto::getArticleId).collect(Collectors.toList());
    final Page<ArticleDocDto> articles = articleSearchService
        .searchArticlesByIdSagSyses(allArticleIds, PageRequest.of(0, SagConstants.MAX_PAGE_SIZE),
            user.isSaleOnBehalf(), ArrayUtils.EMPTY_STRING_ARRAY)
        .map(articleConverter);

   final List<ArticleDocDto> updatedArticles =
        ivdsArticleTaskExecutors.executeTaskWithErpArticle(user, articles.getContent(),
            Optional.empty());

    final Map<VehicleDto, List<ArticleDocDto>> articlesByVehicleMap = new HashMap<>();
    finalCustomerItemsByVehicleMap.forEach((vehId, items) -> {
      final List<String> articleIds = items.stream().map(FinalCustomerOrderItemDto::getArticleId)
          .collect(Collectors.toList());
      final List<ArticleDocDto> filteredArticles = updatedArticles.stream()
          .filter(article -> articleIds.contains(article.getIdSagsys()))
          .collect(Collectors.toList());
      // Update amount number
      final List<ArticleDocDto> updatedAmountArticles = updateAmountNumber(filteredArticles, items);
      final VehicleDto vehicle = vehicles.getOrDefault(vehId, new VehicleDto());
      articlesByVehicleMap.put(vehicle, updatedAmountArticles);
    });

    return articlesByVehicleMap;
  }

  private List<ArticleDocDto> updateAmountNumber(final List<ArticleDocDto> filteredArticles,
      final List<FinalCustomerOrderItemDto> finalUserOrderItemByVehs) {
    final List<ArticleDocDto> updatedArticles = new ArrayList<>();
    for (ArticleDocDto articleDocDto : filteredArticles) {
      final ArticleDocDto clonedArticleDocDto =
          SagBeanUtils.map(articleDocDto, ArticleDocDto.class);
      clonedArticleDocDto
          .setAmountNumber(findAmountNumber(finalUserOrderItemByVehs, articleDocDto.getIdSagsys()));
      updatedArticles.add(clonedArticleDocDto);
    }
    updatedArticles.stream().forEach(art -> updateDisplayedPrice(art, finalUserOrderItemByVehs));
    return updatedArticles;
  }

  private void updateDisplayedPrice(ArticleDocDto article,
      List<FinalCustomerOrderItemDto> finalCustomerOrderItems) {
    finalCustomerOrderItems.stream().filter(FinalCustomerOrderItemDto::hasDisplayedPrice)
        .filter(item -> item.getArticleId().equals(article.getIdSagsys())).findAny()
        .ifPresent(orderItem -> {
          DisplayedPriceDto displayedPrice = DisplayedPriceDto.builder()
              .type(orderItem.getDisplayedPriceType()).brand(orderItem.getDisplayedPriceBrand())
              .brandId(Long.valueOf(orderItem.getDisplayedPriceBrandId())).build();
          article.setDisplayedPrice(displayedPrice);
        });
  }

  private static int findAmountNumber(
      final List<FinalCustomerOrderItemDto> finalCustomerItems,
      final String articleId) {
    return ListUtils.emptyIfNull(finalCustomerItems).stream()
        .filter(item -> StringUtils.equals(item.getArticleId(), articleId))
        .mapToInt(FinalCustomerOrderItemDto::getQuantity)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not found any quantity value"));
  }

  private static List<ShoppingCartRequestBody> buildCartRequests(
      final Map<VehicleDto, List<ArticleDocDto>> articlesByVehicleMap,
      final Long finalCustomerOrgId, String basketItemSourceId, String basketItemSourceDesc) {
    final List<ShoppingCartRequestBody> cartRequests = new ArrayList<>();
    articlesByVehicleMap.forEach((vehicle, articles) -> {
      final List<ShoppingCartRequestBody> cartRequestByVehicle = articles.stream()
          .map(article -> buildCartRequest(vehicle, article, finalCustomerOrgId, basketItemSourceId, basketItemSourceDesc))
          .collect(Collectors.toList());
      cartRequests.addAll(cartRequestByVehicle);
    });
    return cartRequests;
  }

  private static ShoppingCartRequestBody buildCartRequest(VehicleDto vehicle, ArticleDocDto article,
      Long finalCustomerOrgId, String basketItemSourceId, String basketItemSourceDesc) {
    final ShoppingCartRequestBody cartRequest = new ShoppingCartRequestBody();
    cartRequest.setArticle(article);
    cartRequest.setVehicle(vehicle);
    cartRequest.setQuantity(article.getAmountNumber());
    cartRequest.setFinalCustomerOrgId(finalCustomerOrgId);
    cartRequest.setBasketItemSourceId(basketItemSourceId);
    cartRequest.setBasketItemSourceDesc(basketItemSourceDesc);
    return cartRequest;
  }
}
