package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.criteria.BasketHistoryCriteria;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.BasketHistoryService;
import com.sagag.eshop.service.dto.BasketHistoryDto;
import com.sagag.eshop.service.dto.BasketHistoryItemDto;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;
import com.sagag.services.service.api.BasketService;
import com.sagag.services.service.api.CartBusinessService;
import com.sagag.services.service.request.BasketHistoryRequestBody;
import com.sagag.services.service.request.BasketHistorySearchRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class to implement services for saved basket processing.
 */
@Service
@Transactional
public class BasketServiceImpl implements BasketService {

  @Autowired
  private CartBusinessService cartBusinessService;

  @Autowired
  private BasketHistoryService basketHistoryService;

  @Override
  public Page<BasketHistoryDto> getBasketHistories(BasketHistorySearchRequest request,
      final UserInfo user) {
    if (Objects.isNull(user)) {
      throw new IllegalArgumentException("The user info body reuqest must not be null");
    }

    if (!validateBasketScope(user, request)) {
      throw new IllegalArgumentException(
          String.format("Please check your role with Mode %s", request.getBasketFilterMode()));
    }

    // Create basket history search criteria
    final BasketHistoryCriteria criteria = new BasketHistoryCriteria();
    criteria.setOffset(request.getOffset());
    criteria.setPageSize(request.getSize());
    criteria.setBasketName(request.getBasketName());
    criteria.setUpdatedDate(request.getUpdatedDate());
    criteria.setActive(true);
    criteria.setCustomerRefText(request.getCustomerRefText());
    criteria.setCustomerNumber(request.getCustomerNumber());
    criteria.setSalesLastName(StringUtils.defaultString(request.getLastName()));
    criteria.setCustomerName(request.getCustomerName());

    switch (request.getBasketFilterMode()) {
      case ALL_BASKET:
        criteria.setSalesUserId(null);
        break;
      case SALES_BASKET:
        if (user.isSaleOnBehalf()) {
          criteria.setSalesUserId(user.getSalesId());
        } else if (user.isSalesNotOnBehalf()) {
          criteria.setSalesUserId(user.getId());
        }
        break;
      case CUSTOMER_BASKET:
        final String custNrStr = user.getCustNrStr();
        criteria.setCustomerNumber(custNrStr);
        break;
      case USER_BASKET:
      default:
        criteria.setCreatedUserId(user.getOriginalUserId());
        break;
    }

    copyOrderTermsToCriteria(criteria, request);

    final boolean isSalesUser = user.isSalesUser() || user.isSaleOnBehalf();
    return basketHistoryService.searchBasketHistoriesByCriteria(criteria, isSalesUser);
  }

  private boolean validateBasketScope(UserInfo user, BasketHistorySearchRequest request) {
    boolean isValid = false;
    switch (request.getBasketFilterMode()) {
      case ALL_BASKET:
        isValid = user.isSaleOnBehalf() || user.isSalesNotOnBehalf();
        break;
      case SALES_BASKET:
        isValid = user.isSaleOnBehalf() || user.isSalesNotOnBehalf();
        break;
      case CUSTOMER_BASKET:
        isValid = user.isSaleOnBehalf() || user.isUserAdminRole() || user.isNormalUserRole();
        break;
      case USER_BASKET:
        isValid = !user.isSalesUser() && !user.isSaleOnBehalf();
        break;
      default:
        throw new IllegalArgumentException("Filter not is not supported");
    }
    return isValid;
  }

  private static void copyOrderTermsToCriteria(BasketHistoryCriteria criteria,
      BasketHistorySearchRequest request) {
    criteria.setOrderByDescBasketName(request.getOrderByDescBasketName());
    criteria.setOrderByDescCustomerNumber(request.getOrderByDescCustomerNumber());
    criteria.setOrderByDescCustomerName(request.getOrderByDescCustomerName());
    criteria.setOrderByDescGrandTotalExcludeVat(request.getOrderByDescGrandTotalExcludeVat());
    criteria.setOrderByDescUpdatedDate(request.getOrderByDescUpdatedDate());
    criteria.setOrderByDescLastName(request.getOrderByDescLastName());
    criteria.setOrderByDescCustomerRefText(request.getOrderByDescCustomerRefText());
  }

  @Override
  @Transactional
  public void createBasketHistory(BasketHistoryRequestBody request, UserInfo user,
      ShopType shopType) {
    final BasketHistoryDto basketHistory = new BasketHistoryDto();
    // Allow blank basket name, if basket name is blank, we will generate it
    basketHistory.setBasketName(request.getBasketName());
    // Update current user login
    basketHistory.setCreatedUserId(user.getOriginalUserId());
    // If has sales token update user id of sales, for case sales on behalf
    if (user.isSaleOnBehalf()) {
      // Extract user id of sales from sales on behalf token
      basketHistory.setSalesUserId(user.getSalesId());
    }
    final ShoppingCart cartInfo = cartBusinessService.checkoutShopCart(user, shopType, false);
    if (Objects.isNull(cartInfo) || CollectionUtils.isEmpty(cartInfo.getItems())) {
      throw new IllegalArgumentException(
          "Can not create basket history with the empty list of cart items");
    }

    final Map<String, List<ArticleDocDto>> shoppingCartItemsMapByVehicle = new HashMap<>();
    final Map<String, VehicleDto> vehicleMap =
        cartInfo.getItems().stream().collect(Collectors.toMap(ShoppingCartItem::getVehicleId,
            ShoppingCartItem::getVehicle, (vehicle1, vehicle2) -> vehicle1));

    for (final String vehicleId : vehicleMap.keySet()) {
      final List<ArticleDocDto> articles = cartInfo.getItems().stream()
          .filter(item -> vehicleId.equalsIgnoreCase(item.getVehicleId()))
          .map(item -> {
            ArticleDocDto art= item.getArticle();
            art.setBasketItemSourceId(item.getBasketItemSourceId());
            art.setBasketItemSourceDesc(item.getBasketItemSourceDesc());
            // #6753 Add missing info for VIN package
            if (Objects.nonNull(art.getArticle())) {
              if (StringUtils.isEmpty(art.getArtnrDisplay())) {
                art.setArtnrDisplay(art.getArticle().getKeyword());
              }
              if (StringUtils.isEmpty(art.getPartDesc())) {
                art.setPartDesc(art.getArticle().getDescription());
              }
            }
            // #6753 Add missing info for VIN package
            art.setVin(item.isVin());
            art.setItemDesc(art.getArtnrDisplay());
            return  art;
          }).collect(Collectors.toList());
      shoppingCartItemsMapByVehicle.put(vehicleId, articles);
    }

    final List<BasketHistoryItemDto> items = new ArrayList<>();
    shoppingCartItemsMapByVehicle.forEach((vehicleId, articles) -> items
        .add(new BasketHistoryItemDto(vehicleMap.get(vehicleId), articles)));

    double grandTotalExcludeVat = getShoppingCartGrandTotalExcludeVat(request, user, cartInfo);
    basketHistory.setGrandTotalExcludeVat(grandTotalExcludeVat);
    basketHistory.setItems(items);

    basketHistory.setCustomerRefText(request.getCustomerRefText());
    basketHistoryService.createBasketHistory(basketHistory);
  }

  private double getShoppingCartGrandTotalExcludeVat(BasketHistoryRequestBody request,
      UserInfo user, final ShoppingCart cartInfo) {
    double grandTotalExcludeVat =
        BooleanUtils.isTrue(request.getNetPriceViewInContext()) ? cartInfo.getNetTotalExclVat()
            : cartInfo.getGrossTotalExclVat();
    if (user.isFinalUserRole() && BooleanUtils.isTrue(request.getNetPriceViewInContext())) {
      grandTotalExcludeVat = cartInfo.getFinalCustomerNetTotalExclVat();
    }
    return grandTotalExcludeVat;
  }

  @Override
  public long countBasketHistoriesByUser(String customerNr, Long salesId, Long userId) {
    if (Objects.isNull(salesId) && StringUtils.isNotBlank(customerNr) && Objects.isNull(userId)) {
      return NumberUtils.LONG_ZERO;
    }
    if (Objects.nonNull(salesId) && Objects.nonNull(customerNr)) {
      return basketHistoryService.countBasketHistoriesByCustomer(customerNr);
    }

    if (Objects.nonNull(salesId)) {
      return basketHistoryService.countSalesBasketHistories(salesId);
    }

    return basketHistoryService.countBasketHistoriesByUser(userId);
  }

  @Override
  public Optional<BasketHistoryDto> getBasketHistoryDetails(final Long basketId) {
    return basketHistoryService.getBasketHistoryDetails(basketId);
  }

  @Override
  public void delete(final UserInfo user, final Long basketId) {
    Long saleId = null;
    if (user.isSalesUser()) {
      saleId = user.getId();
    } else if (user.isSaleOnBehalf()) {
      saleId = user.getSalesId();
    }

    basketHistoryService.delete(basketId, saleId, user.getOrganisationId(), user.isUserAdminRole(),
        user.getOriginalUserId());
  }
}
