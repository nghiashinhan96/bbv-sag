package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.FitmentArticleDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.VehicleGenArtArtSearchService;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.ivds.api.impl.ArticleProcessor;
import com.sagag.services.ivds.converter.fitment.DefaultFitmentArticleConverter;
import com.sagag.services.ivds.executor.IvdsArticleAmountTaskExecutor;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.utils.FitmentArticleCombinator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class IvdsArticleAmountTaskExecutorImpl extends ArticleProcessor
    implements IvdsArticleAmountTaskExecutor {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;
  @Autowired
  private ContextService contextService;
  @Autowired
  private FitmentArticleCombinator fitmentArticleCombinator;
  @Autowired
  private VehicleGenArtArtSearchService vehGenArtArtSearchService;
  @Autowired
  private DefaultFitmentArticleConverter defFitmentArtConverter;
  @Autowired
  private EshopFavoriteService favoriteBusinessService;
  @Autowired
  private AdditionalCriteriaBuilder additionalCriteriaBuilder;

  /**
   * Return articles including ERP articles, prices information updated.
   *
   * @param user the user who requests
   * @param idSagSys the article id
   * @param amount the amount to update price
   * @return the article updated price
   */
  @Override
  @LogExecutionTime
  public Page<ArticleDocDto> execute(final UserInfo user, final String idSagSys,
      final Integer amount, final Optional<Integer> finalCustomerId) {

    final Page<ArticleDocDto> articles = searchArticlesByArtIdsAndExternalPartArtId(
        Arrays.asList(idSagSys), user.isSaleOnBehalf());
    if (!articles.hasContent()) {
      return Page.empty();
    }

    final Optional<VehicleDto> vehInContext = contextService.getVehicleInContext(user.key());
    if (vehInContext.isPresent()) {
      String vehId = vehInContext.get().getVehId();
      List<String> gaIds = articles.map(ArticleDocDto::getGaId).getContent();
      articles.getContent().stream().map(x -> vehId + x.getGaId()).collect(Collectors.toList());

      // Combine article with merged criteria
      List<FitmentArticleDto> fitmentArticles =
          defFitmentArtConverter.apply(vehGenArtArtSearchService.searchFitments(vehId, gaIds));
      fitmentArticleCombinator.combineArticles(articles, fitmentArticles);
    }
    articles.forEach(article -> article.setAmountNumber(amount));
    final List<ArticleDocDto> articleList = articles.getContent();

    final Optional<String> kTypeNrOpt = vehInContext.map(VehicleDto::getKTypeNrStr);
    List<ArticleDocDto> result = ivdsArticleTaskExecutors.executeTaskWithPriceAndStock(user,
        articleList, vehInContext, finalCustomerId,
        additionalCriteriaBuilder.buildDetailAdditional(idSagSys, kTypeNrOpt));

    return updateFlagFavoriteIntoPage(new PageImpl<>(result), user);
  }

  private Page<ArticleDocDto> updateFlagFavoriteIntoPage(Page<ArticleDocDto> artPage,
      UserInfo user) {
    List<ArticleDocDto> arts =
        Optional.ofNullable(artPage).map(Page::getContent).orElse(Collections.emptyList());
    favoriteBusinessService.updateFavoriteFlagArticles(user, arts);
    return artPage;
  }
}
