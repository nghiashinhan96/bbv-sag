package com.sagag.services.service.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.offer.OfferAddressRepository;
import com.sagag.eshop.repo.api.offer.OfferPersonRepository;
import com.sagag.eshop.repo.api.offer.OfferRepository;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.OfferService;
import com.sagag.eshop.service.converter.OfferConverters;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferDto;
import com.sagag.services.common.enums.offer.OfferConstants;
import com.sagag.services.common.enums.offer.OfferTecStateType;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.executor.IvdsArticleAmountTaskExecutor;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.api.OfferBusinessService;
import com.sagag.services.service.request.offer.OfferPositionItemRequestBody;
import com.sagag.services.service.request.offer.OfferRequestBody;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

@Service
@Slf4j
@Transactional
public class OfferBusinessServiceImpl implements OfferBusinessService {

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private OfferRepository offerRepository;

  @Autowired
  private OfferService offerService;

  @Autowired
  private OfferPersonRepository offerPersonRepo;

  @Autowired
  private OfferAddressRepository offerAddressRepo;

  @Autowired
  private IvdsArticleAmountTaskExecutor ivdsArticleAmountTaskExecutor;

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Override
  public OfferDto updateOffer(final UserInfo user, final OfferRequestBody body) {
    log.debug("Update offer from request = {}", body);

    validateUpdateRequestBody(body);

    final Offer offer = offerService.getOne(user.getOrganisationId(), body.getOfferId())
        .orElseThrow(() -> new ValidationException(
            String.format("Not found offer with id %d", body.getOfferId())));

    offer.setOfferNumber(body.getOfferNumber());
    if (!Objects.isNull(body.getOfferDate())) {
      offer.setOfferDate(body.getOfferDate());
    }

    if (!Objects.isNull(body.getDeliveryDate())) {
      offer.setDeliveryDate(body.getDeliveryDate());
    }

    if (!Objects.isNull(body.getStatus())) {
      offer.setStatus(body.getStatus().name());
    }
    final Date now = Calendar.getInstance().getTime();
    offer.setModifiedUserId(user.getId());
    offer.setModifiedDate(now);
    offer.setVersion(offer.getVersion() + 1);
    offer.setRemark(body.getRemark());
    offer.setTotalGrossPrice(body.getTotalGrossPrice());
    final Long offerPersonId = body.getOfferPersonId();
    if (!Objects.isNull(offerPersonId)) {
      // Set offer person
      final OfferPerson offerPersion = offerPersonRepo.getOne(offerPersonId);
      offer.setRecipient(offerPersion);

      // Set offer address
      final Optional<OfferAddress> offerAddress = offerAddressRepo.findOneByPersonId(offerPersonId);
      offerAddress.ifPresent(offer::setRecipientAddress);
    }

    updateOfferPositions(body.getOfferPositionRequests(), offer, user, now);

    return OfferConverters.convert(offerRepository.save(offer));
  }

  private static void validateUpdateRequestBody(OfferRequestBody body) {
    Assert.notNull(body.getOfferId(), "The given offer id must not be null");
    Assert.notNull(body.getTotalGrossPrice(), "The given total gross price must not be null");
    final List<OfferPositionItemRequestBody> offerPositionItems = body.getOfferPositionRequests();
    if (CollectionUtils.isEmpty(offerPositionItems)) {
      return;
    }
    for (final OfferPositionItemRequestBody offerPositionItemRequestBody : offerPositionItems) {
      Assert.notNull(offerPositionItemRequestBody.getType(), "Type should not be null");
    }
  }

  private static void updateOfferPositions(List<OfferPositionItemRequestBody> offerPositionRequests,
      Offer offer, UserInfo user, final Date now) {

    Set<Long> newOfferPositionIds = new HashSet<>();
    Map<Long, OfferPositionItemRequestBody> newOfferPositionMap = new HashMap<>();
    if (!CollectionUtils.isEmpty(offerPositionRequests)) {
      // #6753 Add missing info for VIN package
      offerPositionRequests.forEach(op -> {
        if (StringUtils.isEmpty(op.getVehicleDescription())) {
          op.setVehicleDescription(op.getArticleDescription());
        }
      });
      newOfferPositionMap = offerPositionRequests.stream()
          .filter(offerPos -> !Objects.isNull(offerPos.getOfferPositionId()))
          .collect(Collectors.toMap(OfferPositionItemRequestBody::getOfferPositionId, x -> x));
      newOfferPositionIds = newOfferPositionMap.keySet();
    }
    final List<OfferPosition> offerPositions = new ArrayList<>(offer.getOfferPositions());
    if (!CollectionUtils.isEmpty(offerPositions)) {
      for (final OfferPosition offerPosition : offerPositions) {
        if (newOfferPositionIds.contains(offerPosition.getId())) {
          // Update existing offer position
          final OfferPositionItemRequestBody offerPositionItem =
              newOfferPositionMap.get(offerPosition.getId());
          offerPosition.setQuantity(offerPositionItem.getQuantity());
          offerPosition.setGrossPrice(offerPositionItem.getGrossPrice());
          offerPosition.setTotalGrossPrice(offerPositionItem.getTotalGrossPrice());
          // discount per each item
          offerPosition.setRemark(offerPositionItem.getRemark());
          offerPosition.setActionType(offerPositionItem.getActionType().name());
          offerPosition.setActionValue(offerPositionItem.getActionValue());
          Optional.ofNullable(offerPositionItem.getDisplayedPrice())
              .ifPresent(fillDisplayedPrice(offerPosition));
          offerPosition.setBasketItemSourceId(offerPositionItem.getBasketItemSourceId());
          offerPosition.setBasketItemSourceDesc(offerPositionItem.getBasketItemSourceDesc());
        } else {
          offerPosition.setTecstate(OfferTecStateType.INACTIVE.name());
        }
        offerPosition.setModifiedUserId(user.getId());
        offerPosition.setModifiedDate(now);
        offerPosition.setVersion(offerPosition.getVersion() + 1);
        offerPosition.setDeliveryDate(offer.getDeliveryDate());
        // #6753 Add missing info for VIN package
        offerPosition.setVehicleDescription(Objects.isNull(offerPosition.getVehicleDescription())
            ? offerPosition.getArticleDescription() : offerPosition.getVehicleDescription());
      }
    }

    offerPositions.addAll(buildNewOfferPositions(offerPositionRequests, offer, user, now));
    offer.setOfferPositions(offerPositions);
  }

  private static Consumer<DisplayedPriceDto> fillDisplayedPrice(final OfferPosition offerPosition) {
    return displayedPrice -> {
      Optional.ofNullable(displayedPrice.getType()).ifPresent(offerPosition::setDisplayedPriceType);
      Optional.ofNullable(displayedPrice.getBrand())
          .ifPresent(offerPosition::setDisplayedPriceBrand);
      Optional.ofNullable(displayedPrice.getBrandId())
          .ifPresent(offerPosition::setDisplayedPriceBrandId);
    };
  }

  private static List<OfferPosition> buildNewOfferPositions(
      final List<OfferPositionItemRequestBody> offerPositionRequests, final Offer offer,
      final UserInfo user, final Date now) {

    final List<OfferPosition> newOfferPositions = new ArrayList<>();
    if (CollectionUtils.isEmpty(offerPositionRequests)) {
      return newOfferPositions;
    }

    for (final OfferPositionItemRequestBody offerPositionRequest : offerPositionRequests) {
      if (offerPositionRequest.isNew()) {
        final OfferPosition newOfferPosition = offerPositionRequest.toOfferPosition();
        newOfferPosition.setOffer(offer);
        newOfferPosition.setCreatedUserId(user.getId());
        newOfferPosition.setCreatedDate(now);
        newOfferPosition.setDeliveryDate(offer.getDeliveryDate());
        newOfferPosition.setTecstate(OfferTecStateType.ACTIVE.name());
        newOfferPosition.setVersion(OfferConstants.FIRST_VERSION);
        newOfferPosition.setCalculated(now);
        newOfferPositions.add(newOfferPosition);
      }
    }
    return newOfferPositions;
  }

  @Override
  public ShoppingCart orderOffer(final UserInfo user,
      final List<OfferPositionItemRequestBody> offerPositionRequests, final ShopType shopType) {
    log.debug("Add all artilces from offer to current shopping basket by user {}",
        user.getUsername());

    final ShoppingCart shoppingCart = cartBusinessService.checkoutShopCart(user, shopType, true);

    if (CollectionUtils.isEmpty(offerPositionRequests)) {
      return shoppingCart;
    }

    final Map<String, ShoppingCartRequestBody> requestMapping = new HashMap<>();

    offerPositionRequests.stream().forEach(offerPos -> {
      final String vehId = offerPos.getVehicleId();
      final String idSagSys = offerPos.getSagsysId();
      final int quantity = offerPos.getQuantity().intValue();

      final String cartKey = CartKeyGenerators.cartKey(user.key(), vehId, idSagSys,
          ShopType.DEFAULT_SHOPPING_CART);
      final ShoppingCartRequestBody existingShoppingCart = requestMapping.get(cartKey);

      if (Objects.isNull(existingShoppingCart)) {
        final ShoppingCartRequestBody shoppingCartRequest = new ShoppingCartRequestBody();
        shoppingCartRequest.setQuantity(quantity);

        final Optional<ArticleDocDto> article =
            getFirstArticleByIdSagSysAndAmount(user, idSagSys, shoppingCartRequest.getQuantity());
        // If article is not found, ignore this article for adding to shopping basket
        if (!article.isPresent()) {
          return;
        }
        ArticleDocDto articleDocDto = article.get();
        updateDisplayedPrice(articleDocDto, offerPos.getDisplayedPrice());
        shoppingCartRequest.setArticle(articleDocDto);
        shoppingCartRequest.setBasketItemSourceId(offerPos.getBasketItemSourceId());
        shoppingCartRequest.setBasketItemSourceDesc(offerPos.getBasketItemSourceDesc());
        ivdsVehicleService.searchVehicleByVehId(vehId).ifPresent(shoppingCartRequest::setVehicle);
        requestMapping.put(cartKey, shoppingCartRequest);
      } else {
        consolidateDisplayedPrices(existingShoppingCart, offerPos);
        // Update quantity for existing item
        final int existedQuantity = existingShoppingCart.getQuantity();
        existingShoppingCart
            .setQuantity(existedQuantity + quantity);

        getFirstArticleByIdSagSysAndAmount(user, idSagSys, existedQuantity)
            .ifPresent(article -> {
              ArticleDocDto existingArticle = existingShoppingCart.getArticle();
              Optional.ofNullable(existingArticle.getDisplayedPrice())
                  .ifPresent(article::setDisplayedPrice);
            });

        existingShoppingCart.setBasketItemSourceId(offerPos.getBasketItemSourceId());
        existingShoppingCart.setBasketItemSourceDesc(offerPos.getBasketItemSourceDesc());
        requestMapping.put(cartKey, existingShoppingCart);
      }
    });

    if (requestMapping.values().isEmpty()) {
      return shoppingCart;
    }
    // Add items to shopping basket
    return cartBusinessService.add(user, Lists.newArrayList(requestMapping.values()),
        ShopType.DEFAULT_SHOPPING_CART);
  }

  private void consolidateDisplayedPrices(ShoppingCartRequestBody existingShoppingCart,
      OfferPositionItemRequestBody offerItem) {
    if (!offerItem.hasSameDisplayedPriceBrand(existingShoppingCart.getDisplayedPrice())) {
      existingShoppingCart.setDisplayedPrice(null);
      existingShoppingCart.getArticle().setDisplayedPrice(null);
    }
  }

  private void updateDisplayedPrice(final ArticleDocDto articleDocDto,
      DisplayedPriceDto displayedPrice) {
    if (displayedPrice != null) {
      articleDocDto.setDisplayedPrice(displayedPrice);
    }
  }

  private Optional<ArticleDocDto> getFirstArticleByIdSagSysAndAmount(final UserInfo user,
      final String idSagSys, final Integer amount) {
    return ivdsArticleAmountTaskExecutor.execute(user, idSagSys, amount, Optional.empty())
        .getContent().stream().findFirst();
  }
}
