package com.sagag.services.dvse.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.ArticlesOrigin;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.dvse.api.UnicatCartService;
import com.sagag.services.dvse.dto.unicat.OrderedItemDto;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.CartItemDto;
import com.sagag.services.hazelcast.domain.cart.CartItemDtoBuilders;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;

import lombok.extern.slf4j.Slf4j;

/**
 * The service for add item to e-Connect shopping cart from UNICAT.
 *
 */
@Service
@Slf4j
@CzProfile
public class UnicatCartServiceImpl extends DvseProcessor implements UnicatCartService {

  @Autowired
  private ArticleSearchExternalExecutor articleSearchExecutor;

  @Autowired
  private CartManagerService cartManagerService;

  @Autowired
  private AttachedArticleSearchExternalExecutor<Map<String, ArticleDocDto>> attachedArticleSearchExecutor;

  @Autowired
  private AttachedArticleRequestBuilder attachedArticleRequestBuilder;

  @LogExecutionTime
  @Override
  public OrderedItemDto addItemsToCart(final UserInfo userInfo, Map<String, Optional<Integer>> articleIdsAndQuantity) {

    final SupportedAffiliate affiliate =
        SupportedAffiliate.fromCompanyName(userInfo.getCompanyName());
    List<ArticleDocDto> articles =
        searchArticles(affiliate, articleIdsAndQuantity, userInfo.isSaleOnBehalf());

    ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .extCustomerId(userInfo.getExternalUserSession().getCustomerId())
        .extLanguage(userInfo.getLanguage())
        .extUsername(userInfo.getExternalUserSession().getUser())
        .extSecurityToken(userInfo.getExternalUserSession().getUid()).articles(articles)
        .updatePrice(true).updateAvailability(false).vatRate(userInfo.getSettings().getVatRate())
        .finalCustomerMarginGroup(userInfo.getSettings().getWssMarginGroup()).build();

    final List<ArticleDocDto> updatedArticles = articleSearchExecutor.execute(criteria);

    updatedArticles.stream().forEach(art -> {
      art.setOrigin(ArticlesOrigin.UNICAT);
      addItemToCart(userInfo, art, art.getAmountNumber());
    });

    List<String> updatedArticleIds =
        updatedArticles.stream().map(ArticleDocDto::getArtid).collect(Collectors.toList());

    final List<ArticleDocDto> notExistArticles =
        articles.stream().filter(elasticArt -> !updatedArticleIds.contains(elasticArt.getArtid()))
            .collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(notExistArticles)) {
      notExistArticles.stream()
          .forEach(article -> addItemToCart(userInfo, article, article.getAmountNumber()));
    }
    return new OrderedItemDto();
  }

  private void addItemToCart(UserInfo user, ArticleDocDto article, Integer quantity) {
    final ShoppingCartRequestBody request = new ShoppingCartRequestBody();
    request.setArticle(article);
    request.setQuantity(quantity);

    cartManagerService.add(createCartItem(user, request));

    cartManagerService.updateTotalCache(user.getCachedUserId(), user.getCustNrStr(),
        ShopType.DEFAULT_SHOPPING_CART);
  }

  private CartItemDto createCartItem(UserInfo user, ShoppingCartRequestBody request) {
    final ArticleDocDto artDto = request.getArticle();
    final String cartKey = CartKeyGenerators.cartKey(user.key(), request.vehId(),
        request.idSagsys(), artDto.getSupplierId(), artDto.getSupplierArticleNumber(),
        ShopType.DEFAULT_SHOPPING_CART);

    final CartItemDto addedCartItem = cartManagerService.findByKey(user.key(), cartKey)
        .map(changeQuantity(request)).orElseGet(generateCartItem(cartKey, user, request));

    Optional.ofNullable(artDto).filter(ArticleDocDto::hasAttachedArticle)
        .ifPresent(art -> updateAttachedArticles(user, addedCartItem));

    return addedCartItem;
  }

  private Function<CartItemDto, CartItemDto> changeQuantity(ShoppingCartRequestBody request) {
    return item -> {
      log.debug("Update article info with cart item");
      item.setQuantity(request.getQuantity());
      return item;
    };
  }

  private Supplier<CartItemDto> generateCartItem(final String cartKey, final UserInfo user,
      final ShoppingCartRequestBody request) {
    return () -> {
      log.debug("Add new article info with cart item");
      return CartItemDtoBuilders.build(cartKey, user, request, ShopType.DEFAULT_SHOPPING_CART);
    };
  }

  private void updateAttachedArticles(UserInfo user, CartItemDto addedCartItem) {

    final VehicleDto vehicle = addedCartItem.getVehicle();
    final List<AttachedArticleRequest> attachedAticleRequestList = attachedArticleRequestBuilder
        .buildAttachedAticleRequestList(addedCartItem, ShopType.DEFAULT_SHOPPING_CART);
    if (CollectionUtils.isEmpty(attachedAticleRequestList)) {
      return;
    }
    final WssDeliveryProfileDto wssDeliveryProfile = user.getSettings().getWssDeliveryProfile();
    AttachedArticleSearchCriteria criteria = AttachedArticleSearchCriteria
        .createcreateArticleRequest(SupportedAffiliate.fromCompanyName(user.getCompanyName()),
            user.getCustomer(), attachedAticleRequestList, vehicle, wssDeliveryProfile);
    criteria.setPriceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum());
    criteria.setFinalCustomerUser(user.isFinalUserRole());
    criteria.setFinalCustomerHasNetPrice(user.isFinalCustomerHasNetPrice());

    Map<String, ArticleDocDto> attachedArticles = attachedArticleSearchExecutor.execute(criteria);
    if (MapUtils.isEmpty(attachedArticles)) {
      return;
    }

    addedCartItem
        .setAttachedArticles(attachedArticles.values().stream().collect(Collectors.toList()));
  }
}
