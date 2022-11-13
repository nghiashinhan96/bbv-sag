package com.sagag.services.ax.callable.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ChunkedRequestProcessor;
import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.ax.callable.AvailabilityAsyncCallableCreator;
import com.sagag.services.ax.request.AxAvailabilityRequestBuilder;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@DynamicAxProfile
public class AxAvailabilityAsyncCallableCreatorImpl implements AvailabilityAsyncCallableCreator,
  ChunkedRequestProcessor<ArticleDocDto> {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private AppProperties appProps;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updateAvailability(criteria);
    };
  }

  private List<ArticleDocDto> updateAvailability(final ArticleSearchCriteria criteria) {
    return processPartitionsByList(criteria.getArticles(), updateAvailabilityProcessor(criteria));
  }

  /**
   * Update availability DTO based on the current criteria It will be handled for a list or single
   * article case.
   *
   * @param criteria
   * @return List<ArticleDocDto>
   */
  private UnaryOperator<List<ArticleDocDto>> updateAvailabilityProcessor(
      ArticleSearchCriteria criteria) {
    return articles -> {
      final boolean isFetchAll = isForceFetchAll(criteria);
      final List<ArticleDocDto> shouldGetAvaiArticles = isFetchAll ? articles
          : articles.stream().filter(ArticleDocDto::isOnStock).collect(Collectors.toList());

      final List<ArticleDocDto> shouldNotGetAvaiArticles = isFetchAll ? Collections.emptyList()
          : articles.stream().filter(ArticleDocDto::isNotOnStock).collect(Collectors.toList());

      if (CollectionUtils.isEmpty(shouldGetAvaiArticles)) {
        return shouldNotGetAvaiArticles;
      }
      String deliveryType = criteria.getDeliveryType();
      if (criteria.isFinalCustomerUser()) {
        deliveryType = ErpSendMethodEnum.TOUR.name();
      }
      final Collection<ArticleRequest> articleReq =
          AxArticleUtils.prepareArticleRequests(shouldGetAvaiArticles);
      final BasketPositionRequest basket = new BasketPositionRequest(articleReq, Optional.empty());
      final AxAvailabilityRequestBuilder requestBuilder =
          new AxAvailabilityRequestBuilder(criteria.getCustNr(), Collections.singletonList(basket))
              .addressId(criteria.getAddressId()).sendMethod(deliveryType)
              .availabilityUrl(criteria.getAvailabilityUrl())
              .partialDelivery(criteria.isPartialDelivery())
              .pickupBranchId(criteria.getPickupBranchId());
      // apply only for TOUR send method. isTourTimetable is always FALSE in PICKUP
      log.debug("isTourTimetable is {}", requestBuilder.isTourTimetable());
      try {
        final Map<String, List<Availability>> pimAvailsMap =
            articleExtService.searchAvailabilities(requestBuilder.build());

        // Update availability info to article
        final List<ArticleDocDto> result = new ArrayList<>(shouldGetAvaiArticles);
        result.forEach(article -> article.setAvailabilities(pimAvailsMap.get(article.getIdSagsys())));

        return ListUtils.union(result, shouldNotGetAvaiArticles);
      } catch (final RestClientException ex) {
        log.error("Update article availability from ERP has exception: ", ex);
        return new ArrayList<>(articles);
      }
    };
  }

  private static boolean isForceFetchAll(ArticleSearchCriteria criteria) {
    return criteria.isFetchAll() || !criteria.isUpdateStock() && criteria.isUpdateAvailability();
  }

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.AVAILABILITY;
  }

  @Override
  public int maxRequestSize() {
    return appProps.getErpConfig().getMaxRequestSize();
  }
}
