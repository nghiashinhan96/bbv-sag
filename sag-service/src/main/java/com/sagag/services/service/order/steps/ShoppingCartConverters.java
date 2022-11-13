package com.sagag.services.service.order.steps;

import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.eshop.service.dto.order.FinalCustomerOrderItemDto;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Asserts;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ShoppingCartConverters {

  public static List<FinalCustomerOrderItem> converter(ShoppingCart shoppingCart,
      boolean allowModifyAvailHistory) {
    return shoppingCart.getItems().stream()
        .map(shoppingCartItem -> convertShoppingCartItemToFinalCustomerOrderItem(shoppingCartItem,
            allowModifyAvailHistory))
        .collect(Collectors.toList());
  }

  private static FinalCustomerOrderItem convertShoppingCartItemToFinalCustomerOrderItem(
      ShoppingCartItem shoppingCartItem, boolean allowModifyAvailHistory) {

    final ArticleDocDto articleDocDto = shoppingCartItem.getArticleItem();
    Asserts.notNull(articleDocDto, "articleDocDto should not be null");

    String availabilities = StringUtils.EMPTY;
    if (allowModifyAvailHistory) {
      availabilities = SagJSONUtil.convertObjectToJson(articleDocDto.getAvailabilities());
    }

    final String genArtDescription =
        CollectionUtils.isEmpty(articleDocDto.getGenArtTxts()) ? StringUtils.EMPTY
            : articleDocDto.getGenArtTxts().get(0).getGatxtdech();

    List<FinalCustomerOrderItemDto> attachItems = CollectionUtils.emptyIfNull(
        shoppingCartItem.getAttachedCartItems())
            .stream().map(item -> convertAttachedCartItem(allowModifyAvailHistory, item))
            .collect(Collectors.toList());


    return FinalCustomerOrderItem.builder()
        .vehicleId(shoppingCartItem.getVehicleId())
        .articleId(articleDocDto.getIdSagsys()).vehicleDesc(shoppingCartItem.getVehicleInfo())
        .articleDesc(articleDocDto.getArtnrDisplay())
        .type(Optional.ofNullable(shoppingCartItem.getItemType()).map(Objects::toString)
            .orElse(null))
        .genArtDescription(genArtDescription).supplier(articleDocDto.getSupplier())
        .brand(articleDocDto.getIdDlnr())
        .images(SagJSONUtil.convertObjectToJson(articleDocDto.getImages()))
        .productAddon(articleDocDto.getProductAddon())
        .reference(StringUtils.defaultString(shoppingCartItem.getReference(), StringUtils.EMPTY))
        .grossPrice(shoppingCartItem.getGrossPriceIgnoreDisplayedPrice())
        .netPrice(shoppingCartItem.getNetPrice())
        .quantity(shoppingCartItem.getQuantity())
        .salesQuantity(shoppingCartItem.getSalesQuantity())
        .displayedPriceType(Optional.ofNullable(articleDocDto.getDisplayedPrice())
            .map(DisplayedPriceDto::getType).orElse(null))
        .displayedPriceBrand(Optional.ofNullable(articleDocDto.getDisplayedPrice())
            .map(DisplayedPriceDto::getBrand).orElse(null))
        .displayedPriceBrandId(Optional.ofNullable(articleDocDto.getDisplayedPrice())
            .map(item -> String.valueOf(item.getBrandId())).orElse(null))
        .availabilities(availabilities)
        .finalCustomerNetPrice(shoppingCartItem.getFinalCustomerNetPrice())
        .grossPriceWithVat(shoppingCartItem.getGrossPriceWithVat())
        .finalCustomerNetPriceWithVat(shoppingCartItem.getFinalCustomerNetPriceWithVat())
        .attachItems(SagJSONUtil.convertObjectToJson(attachItems))
        .build();
  }

  private static FinalCustomerOrderItemDto convertAttachedCartItem(boolean allowModifyAvailHistory,
          ShoppingCartItem item) {
    FinalCustomerOrderItemDto finalCustomerOrderItemDto = new FinalCustomerOrderItemDto(
        convertShoppingCartItemToFinalCustomerOrderItem(item, allowModifyAvailHistory));
    finalCustomerOrderItemDto.setDepot(item.isDepot());
    finalCustomerOrderItemDto.setPfand(item.isPfand());
    finalCustomerOrderItemDto.setVrg(item.isVrg());
    finalCustomerOrderItemDto.setVoc(item.isVoc());
    return finalCustomerOrderItemDto;
  }
}
