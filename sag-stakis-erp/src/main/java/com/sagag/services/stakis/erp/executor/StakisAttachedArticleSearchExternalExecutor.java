package com.sagag.services.stakis.erp.executor;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.article.api.executor.AttachedArticleSearchCriteria;
import com.sagag.services.article.api.executor.AttachedArticleSearchExternalExecutor;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.stakis.erp.domain.TmAttachedArticleRequest;

@Component
@CzProfile
public class StakisAttachedArticleSearchExternalExecutor
  implements AttachedArticleSearchExternalExecutor<Map<String, ArticleDocDto>> {

  @Autowired
  private FinalCustomerPriceCalculator finalCustomerPriceCalculator;

  @Override
  public Map<String, ArticleDocDto> execute(AttachedArticleSearchCriteria criteria) {
    Assert.notNull(criteria, "The given criteria must not be null.");
    Assert.notEmpty(criteria.getAttachedArticleRequestList(),
        "The given attached article request list must not be empty.");
    return criteria.getAttachedArticleRequestList().stream()
        .map(TmAttachedArticleRequest.class::cast)
        .map(requestItem -> {
          requestItem.getDepositArticle().setAmountNumber(requestItem.getQuantity());
          requestItem.getDepositArticle().setSalesQuantity(requestItem.getSalesQuantity());

          reCalculateAttachedArticlePrice(requestItem.getDepositArticle().getPrice(),
              requestItem.getDepositArticle().getAmountNumber());
          doCalculatePriceForFinalCustomer(requestItem.getDepositArticle(), criteria);
          return requestItem;
        }).collect(Collectors.toMap(
            TmAttachedArticleRequest::getCartKey, TmAttachedArticleRequest::getDepositArticle));
  }

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    return finalCustomerPriceCalculator;
  }


  @Override
  public boolean isCallShoppingCart() {
    return true;
  }

}
