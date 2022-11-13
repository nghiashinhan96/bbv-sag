package com.sagag.services.dvse.api.impl;

import java.util.List;
import java.util.Map;
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
import com.sagag.services.dvse.api.AxCzDvseCartService;
import com.sagag.services.dvse.builder.AxCzDvseObjectHelper;
import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.profiles.AxCzDvseProfile;
import com.sagag.services.dvse.utils.DvseArticleUtils;
import com.sagag.services.dvse.wsdl.tmconnect.ArticleTmf;
import com.sagag.services.dvse.wsdl.tmconnect.MasterData;
import com.sagag.services.dvse.wsdl.tmconnect.OrderPosition;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrder;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderResponse;

/**
 * The service for add item to e-Connect shopping cart from DVSE.
 *
 */
@Service
@AxCzDvseProfile
public class AxCzDvseCartServiceImpl extends DvseProcessor implements AxCzDvseCartService {

  @Autowired
  private ArticleSearchExternalExecutor articleSearchExecutor;

  @Autowired
  private DvseCartHandler dvseCartHandler;

  @LogExecutionTime
  @Override
  public SendOrderResponse addItemsToCart(ConnectUser user, SendOrder order) {
    Optional<MasterData> masterDataOpt = Optional.ofNullable(order.getRequest().getMasterData());
    Map<String, String> articleIdAndUUIDMap =
        AxCzDvseObjectHelper.extractArticleIdUUIDMap(masterDataOpt);
    List<ArticleTmf> artTmfs = AxCzDvseObjectHelper.extractArticleTmfs(masterDataOpt);
    List<OrderPosition> orderPositions = AxCzDvseObjectHelper.extractOrderPosition(order);
    List<ArticleDocDto> addToCartArticles = AxCzDvseObjectHelper
        .buildArticleFromOrderRequest(articleIdAndUUIDMap, artTmfs, orderPositions);
    Map<String, Optional<Integer>> articleAndRequestQuantity =
        DvseArticleUtils.buildArticleAndRequestedQuantityMap(addToCartArticles);

    final SupportedAffiliate affiliate = user.getAffiliate();
    final List<ArticleDocDto> articles =
        searchArticles(affiliate, articleAndRequestQuantity, user.isSaleOnBehalf());

    if (CollectionUtils.isEmpty(articles)) {
      addToCartArticles.forEach(article -> dvseCartHandler.addItemToCart(user, article,
          Optional.empty(), article.getAmountNumber()));
      return AxCzDvseObjectHelper.buildAddToBasketResponse(order);
    }

    final ArticleSearchCriteria criteria = ArticleSearchCriteria.builder()
        .affiliate(user.getAffiliate())
        .custNr(user.getCustNrStr())
        .filterArticleBefore(true)
        .updatePrice(true)
        .isCartMode(false)
        .updateStock(false)
        .updateAvailability(false)
        .priceTypeDisplayEnum(user.getSettings().getPriceTypeDisplayEnum())
        .articles(articles)
        .companyName(user.getCompanyName())
        .finalCustomerMarginGroup(user.getSettings().getWssMarginGroup())
        .isDvseMode(true)
        .build();

    final List<ArticleDocDto> updatedArticles = articleSearchExecutor.execute(criteria);

    updatedArticles.stream().forEach(item -> {
      item.setOrigin(ArticlesOrigin.FROM_TECCAT);
      dvseCartHandler.addItemToCart(user, item, Optional.empty(), item.getAmountNumber());
    });
    return AxCzDvseObjectHelper.buildAddToBasketResponse(order);
  }

}
