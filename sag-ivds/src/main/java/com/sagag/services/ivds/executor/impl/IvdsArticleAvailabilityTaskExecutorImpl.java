package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.utils.DefaultAmountHelper;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.ivds.api.impl.ArticleProcessor;
import com.sagag.services.ivds.executor.IvdsArticleAvailabilityTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.request.ArticleInformationRequestItem;
import com.sagag.services.ivds.request.availability.GetArticleInformation;
import com.sagag.services.ivds.response.GetArticleInformationResponse;
import com.sagag.services.ivds.response.availability.AvailabilityResponseItem;
import com.sagag.services.ivds.response.availability.GetArticleAvailabilityResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class IvdsArticleAvailabilityTaskExecutorImpl extends ArticleProcessor
  implements IvdsArticleAvailabilityTaskExecutor {

  @Autowired
  private ContextService contextService;
  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;
  @Autowired
  private AdditionalCriteriaBuilder additionalCriteriaBuilder;

  /**
   * Return articles including ERP articles, availabilities information updated.
   *
   * @param user user how requests
   * @param idSagSys specific article isSagSys
   * @param amountNumber article amount number
   * @return the {@link List<ArticleDocDto>}
   */
  @Override
  public List<ArticleDocDto> executeAvailability(final UserInfo user, final String idSagSys,
      final Integer amountNumber, ArticleStock stock, Double totalAxStock) {
    final Page<ArticleDocDto> articles = searchArticlesByArtIdsAndExternalPartArtId(
        Arrays.asList(idSagSys), user.isSaleOnBehalf());
    if (!articles.hasContent()) {
      return Collections.emptyList();
    }
    if (!user.hasCust()) {
      return Collections.unmodifiableList(articles.getContent());
    }

    final Optional<VehicleDto> vehInContext = contextService.getVehicleInContext(user.key());
    articles.forEach(a -> {
      a.setAmountNumber(findAmountNumber(amountNumber, a, vehInContext));
      a.setStock(stock);
      a.setTotalAxStock(totalAxStock);
    });

    return ivdsArticleTaskExecutors.executeTaskWithAvailability(user, articles.getContent(),
        vehInContext, additionalCriteriaBuilder.build(Optional.ofNullable(idSagSys),
            Optional.empty(), true, null));
  }

  /**
   * Returns updated availabilities information.
   *
   * @param user user how requests
   * @param request the request
   * @return the object of {@link GetArticleInformationResponse}
   */
  @Override
  public GetArticleAvailabilityResponse executeAvailabilities(final UserInfo user,
      final GetArticleInformation request) {
    final List<ArticleInformationRequestItem> items = request.getAvailabilityRequestItemList();
    if (CollectionUtils.isEmpty(items) || items.size() > request.getNumberOfRequestedItems()) {
      throw new IllegalArgumentException("The given list of items must be valid");
    }

	final List<String> pimIds = items.stream().map(ArticleInformationRequestItem::getIdPim)
		 .collect(Collectors.toList());
    final Page<ArticleDocDto> articles = searchArticlesByArtIdsAndExternalPartArtId(pimIds,
        user.isSaleOnBehalf());
    if (!articles.hasContent()) {
      return GetArticleAvailabilityResponse.builder().build();
    }

    final Optional<VehicleDto> vehInContext = contextService.getVehicleInContext(user.key());
    articles.forEach(article -> {
      final List<ArticleInformationRequestItem> requestItems =
          items.stream().filter(item -> StringUtils.equals(item.getIdPim(), article.getIdSagsys()))
              .collect(Collectors.toList());
      final int totalAmount = requestItems.stream()
          .collect(Collectors.summingInt(ArticleInformationRequestItem::getQuantity));
      final ArticleInformationRequestItem requestItem = requestItems.stream().findFirst()
          .orElse(null);
      final int amountNumber = requestItem == null ? 0 : totalAmount;
      article.setAmountNumber(findAmountNumber(amountNumber, article, vehInContext));
      article.setStock(requestItem.getStock());
      article.setTotalAxStock(requestItem.getTotalAxStock());
    });

    Optional<AdditionalSearchCriteria> additional =
        additionalCriteriaBuilder.buildDetailAdditional(pimIds);
    final List<ArticleDocDto> articleDtoList = ivdsArticleTaskExecutors.executeTaskWithAvailability(
        user, articles.getContent(), vehInContext, additional);
    return GetArticleAvailabilityResponse.builder()
        .items(responseConverter().apply(articleDtoList)).build();
  }

  private static Function<List<ArticleDocDto>,
    Map<String, AvailabilityResponseItem>> responseConverter() {
    return articleDtoList -> articleDtoList.stream()
        .collect(Collectors.toMap(ArticleDocDto::getIdSagsys, article -> AvailabilityResponseItem
            .builder().availabilities(article.getAvailabilities()).build()));
  }

  private static Integer findAmountNumber(final Integer amountNumber, final ArticleDocDto art,
      final Optional<VehicleDto> veh) {
    if (Objects.isNull(amountNumber) || amountNumber <= 0) {
      // find the default sales quantity which was defined/configured in Connect Shop
      return DefaultAmountHelper.getArticleSalesQuantity(art.getGaId(), veh);
    }
    return amountNumber;
  }

}
