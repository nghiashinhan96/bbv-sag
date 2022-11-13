package com.sagag.services.article.api.executor;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.AvailabilitiesFilterContext;
import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerDeliveryTimeCalculator;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DefaultArticleSearchExternalExecutor implements ArticleSearchExternalExecutor {

  private static final String[] DISABLED_GET_VENDOR_LIST = ArrayUtils.EMPTY_STRING_ARRAY;

  private static final int ZERO_REQUEST_SIZE = 0;

  @Autowired
  private ArticleErpExternalExecutorBuilders artErpExternalExecutorBuilders;

  @Autowired
  private AvailabilitiesFilterContext availabilitiesFilterContext;

  @Autowired
  private FinalCustomerPriceCalculator finalCustomerPriceCalculator;

  @Autowired
  private FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator;

  @Override
  @LogExecutionTime(infoMode = true)
  public List<ArticleDocDto> execute(ArticleSearchCriteria criteria) {
    final ServletRequestAttributes attributes =
      (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    Optional.ofNullable(artErpExternalExecutorBuilders.buildAsyncCallableCreator(criteria,
      Lists.newArrayList(DISABLED_GET_VENDOR_LIST), ZERO_REQUEST_SIZE, attributes))
      .ifPresent(CompletableFuture::join);

    return availabilitiesFilterContext.doFilterAvailabilities(criteria.getArticles(), criteria);
  }

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    return finalCustomerPriceCalculator;
  }

  @Override
  public FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator() {
    return finalCustomerDeliveryCalculator;
  }
}
