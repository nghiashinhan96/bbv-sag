package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.attachedarticle.AttachedArticleHandler;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.utils.DefaultAmountHelper;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.ivds.api.impl.ArticleProcessor;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;
import com.sagag.services.ivds.executor.IvdsArticleErpInformationTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.request.ArticleInformationRequestItem;
import com.sagag.services.ivds.request.GetArticleSyncInformation;
import com.sagag.services.ivds.response.ArticleInformationResponseItem;
import com.sagag.services.ivds.response.GetArticleInformationResponse;
import com.sagag.services.ivds.utils.ArticleInformationExtractor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class IvdsArticleErpInformationTaskExecutorImpl extends ArticleProcessor
    implements IvdsArticleErpInformationTaskExecutor {

  @Autowired
  private ContextService contextService;

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private AbstractIvdsArticleTaskExecutor abstractIvdsArticleTaskExecutor;

  @Autowired
  private ArticleInformationExtractor articleInformationExtractor;

  @Autowired
  private AttachedArticleHandler attachedArticleHandler;

  @Override
  public GetArticleInformationResponse executeErpInformation(final UserInfo user,
      final GetArticleSyncInformation request) {

    final List<ArticleInformationRequestItem> items = request.getArticleInformationRequestItems();
    if (CollectionUtils.isEmpty(items) || items.size() > request.getNumberOfRequestedItems()) {
      throw new IllegalArgumentException("The given list of items must be valid");
    }

    final List<String> pimIds = items.stream().map(ArticleInformationRequestItem::getIdPim)
        .collect(Collectors.toList());
    final Page<ArticleDocDto> articles = searchArticlesByArtIdsAndExternalPartArtId(pimIds,
        user.isSaleOnBehalf());

    if (!articles.hasContent()) {
      if (user.getSupportedAffiliate().isAutonetAffiliate()) {
        Map<String, ArticleInformationResponseItem> resItems = new HashMap<>();
        items.stream().filter(item -> !StringUtils.isBlank(item.getIdPim()))
            .forEach(item -> resItems.put(
                item.getIdPim(), ArticleInformationResponseItem.builder().build()));
        return GetArticleInformationResponse.builder().items(resItems).build();
      }
      throw new IllegalArgumentException("Not found any articles from articles catalog");
    }

    final Optional<VehicleDto> vehInContext = contextService.getVehicleInContext(user.key());
    articles.forEach(article -> {
      final List<ArticleInformationRequestItem> requestItems =
          items.stream().filter(item -> StringUtils.equals(item.getIdPim(), article.getIdSagsys()))
              .collect(Collectors.toList());
      final int totalAmount = requestItems.stream()
          .collect(Collectors.summingInt(ArticleInformationRequestItem::getQuantity));
      final ArticleInformationRequestItem requestItem =
          requestItems.stream().findFirst().orElse(null);
      final int amountNumber = requestItem == null ? 0 : totalAmount;
      article.setAmountNumber(findAmountNumber(amountNumber, article, vehInContext));
      article.setStock(requestItem.getStock());
      article.setTotalAxStock(requestItem.getTotalAxStock());
    });

    Optional<AdditionalSearchCriteria> additionalSearchCriteria =
        additionalCriBuilder.buildVehicleAdditional(vehInContext.map(VehicleDto::getKTypeNrStr));
    List<ArticleDocDto> updatedArticlesWithErpInformation =
        ivdsArticleTaskExecutors.executeTaskWithSpecificErpInfoRequest(user, articles.getContent(),
            vehInContext, request.getErpInfoRequest(), additionalSearchCriteria);
    
    final ArticleExternalRequestOption.ArticleExternalRequestOptionBuilder optionBuilder =
        ivdsArticleTaskExecutors.extractErpSyncInfoRequest(request.getErpInfoRequest(),
            additionalSearchCriteria);

    final ArticleSearchCriteria.ArticleSearchCriteriaBuilder builder =
        abstractIvdsArticleTaskExecutor.initArticleSearchCriteriaBuilder(user,
            articles.getContent(), vehInContext.orElse(null), optionBuilder.build());

    final ArticleSearchCriteria criteria = builder.build();
    attachedArticleHandler.buildMemoList(updatedArticlesWithErpInformation, criteria);

    // Update missing article for erp-sync
    return GetArticleInformationResponse.builder()
        .items(CollectionUtils.emptyIfNull(updateMissingArticles(pimIds, updatedArticlesWithErpInformation)).stream()
            .collect(Collectors.toMap(ArticleDocDto::getArtid,
                art -> articleInformationExtractor.buildErpInformation(art), (art1, art2) -> art2)))
        .build();
  }

  private static Integer findAmountNumber(final Integer amountNumber, final ArticleDocDto art,
      final Optional<VehicleDto> veh) {
    if (Objects.isNull(amountNumber) || amountNumber <= 0) {
      // find the default sales quantity which was defined/configured in Connect Shop
      return DefaultAmountHelper.getArticleSalesQuantity(art.getGaId(), veh);
    }
    return amountNumber;
  }

  private List<ArticleDocDto> updateMissingArticles(List<String> originalIds,
      List<ArticleDocDto> erpArticles) {
    List<ArticleDocDto> updatedArticles = new ArrayList<>(erpArticles);
    List<String> erpIds = erpArticles.stream().map(ArticleDocDto::getIdSagsys).collect(Collectors.toList());
    List<String> missingIds = originalIds.stream().filter(id -> !erpIds.contains(id)).collect(Collectors.toList());
    missingIds.forEach(id -> {
      ArticleDocDto doc = new ArticleDocDto();
      doc.setIdSagsys(id);
      doc.setArtid(id);
      updatedArticles.add(doc);
    });
    return updatedArticles;
  }
}
