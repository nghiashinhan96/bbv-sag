package com.sagag.services.dvse.api.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.ArticlesOrigin;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.dvse.api.DvseCartService;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.dto.dvse.OrderedItemDto;
import com.sagag.services.dvse.profiles.DefaultDvseProfile;
import com.sagag.services.dvse.utils.DvseUtils;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfItem;
import com.sagag.services.dvse.wsdl.dvse.Item;
import com.sagag.services.dvse.wsdl.dvse.Order;

/**
 * The service for add item to e-Connect shopping cart from DVSE.
 *
 */
@Service
@DefaultDvseProfile
public class DvseCartServiceImpl extends DvseProcessor implements DvseCartService {

  @Autowired
  private ArticleSearchExternalExecutor articleSearchExecutor;

  @Autowired
  private DvseCartHandler dvseCartHandler;

  @LogExecutionTime
  @Override
  public OrderedItemDto addItemsToCart(ConnectUser user, Order order, ArrayOfItem items) {

    // Step 1: Search vehicle and article info from ES
    // Search articles
    final List<Item> itemList = items.getItem();
    final SupportedAffiliate affiliate = user.getAffiliate();
    final Optional<VehicleDto> vehicle = searchVehicle(DvseUtils.getKtypeNr(itemList));
    final List<ArticleDocDto> articles = searchArticles(affiliate,
        DvseUtils.getPimIdAndQuantities(itemList), user.isSaleOnBehalf());

    // Add items not found in ES to non-ref mode
    if (CollectionUtils.isEmpty(articles)) {
      itemList.stream().map(DvseUtils.nonReferenceArticleConverter())
      .forEach(article -> dvseCartHandler.addItemToCart(user, article, vehicle, article.getAmountNumber()));
      return new OrderedItemDto(order.getOrderId());
    }

    // Step 2: Get latest ERP info: get article info and price info
    final ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .affiliate(user.getAffiliate())
        .custNr(user.getCustNrStr())
        .filterArticleBefore(true)
        .updatePrice(true)
        .isCartMode(false)
        .updateStock(false)
        .updateAvailability(false)
        .vehicle(vehicle.orElse(null))
        .priceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum())
        .articles(articles)
        .companyName(user.getCompanyName())
        .finalCustomerMarginGroup(user.getSettings().getWssMarginGroup())
        .isDvseMode(true)
        .build();

    final List<ArticleDocDto> updatedArticles = articleSearchExecutor.execute(criteria);

    // Step 3: Add article item to e-Connect shopping cart
    // Add items to cart
    // Set Origin from TECCAT
    updatedArticles.stream().forEach(item -> {
      item.setOrigin(ArticlesOrigin.FROM_TECCAT);
      dvseCartHandler.addItemToCart(user, item, vehicle, item.getAmountNumber());
    });
    return new OrderedItemDto(order.getOrderId());
  }

}
