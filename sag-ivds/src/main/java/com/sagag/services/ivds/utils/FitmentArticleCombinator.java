package com.sagag.services.ivds.utils;

import com.sagag.services.domain.article.ArticleCriteriaDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.FitmentArticleDto;
import com.sagag.services.elasticsearch.api.VehicleGenArtArtSearchService;
import com.sagag.services.elasticsearch.domain.category.GtCUPIInfo;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleGenArtArtDoc;
import com.sagag.services.ivds.converter.fitment.DistinctedFitmentArticleConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FitmentArticleCombinator {

  private static final String CID_100_BY_DEFAULT = "100";

  @Autowired
  private VehicleGenArtArtSearchService vehGenArtArtSearchService;

  @Autowired
  private DistinctedFitmentArticleConverter distFitmentArtConverter;

  public void combineArticles(List<ArticleDocDto> articles, List<FitmentArticleDto> fitmentArticles,
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi) {
    combineFitmentArticles(articles, fitmentArticles, articlesByCupi);
  }

  public void combineArticles(Page<ArticleDocDto> articles,
      List<FitmentArticleDto> fitmentArticles) {
    articles.forEach(fitmentCombinator(fitmentArticles, new HashMap<>()));
  }

  /**
   * Change the criteria of article Combine criteria from articles_de and vehicles_genart_art. If
   * there is a duplicate criteria, then vehcile_genart_art takes priority
   *
   * @param articlesByCupi
   *
   * @param article the article
   * @param genArtArtArticles the genArtArtArticles
   */
  private void combineFitmentArticles(List<ArticleDocDto> articles,
      List<FitmentArticleDto> fitmentArticles,
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi) {
    // Combine article with merged criteria
    articles.forEach(fitmentCombinator(fitmentArticles, articlesByCupi));
  }

  private Consumer<ArticleDocDto> fitmentCombinator(List<FitmentArticleDto> fitmentArticles,
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi) {
    return article -> {
      final String articleId = article.getId();
      final Optional<FitmentArticleDto> fitmentArticle =
          CollectionUtils.emptyIfNull(fitmentArticles).stream()
              .filter(ga -> StringUtils.equalsIgnoreCase(ga.getArtId(), articleId)).findFirst();
      combineFitmentArticle(article, fitmentArticle, articlesByCupi);
    };
  }

  private void combineFitmentArticle(ArticleDocDto article,
      Optional<FitmentArticleDto> fitmentArticleOpt,
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi) {
    if (article == null) {
      return;
    }

    // #4271 manually attach cid100 to criterias of returned article if that article is not indexed
    // in fitment yet
    if (!fitmentArticleOpt.isPresent()) {
      GtCUPIInfo selectedCupi = MapUtils.emptyIfNull(articlesByCupi).entrySet().stream()
          .filter(artByCupi -> artByCupi.getValue().stream()
              .anyMatch(art -> StringUtils.equals(art.getArtid(), article.getArtid())))
          .map(Entry::getKey).findFirst().orElse(null);
      if (Objects.nonNull(selectedCupi)) {
        article.addCriterias(Arrays.asList(createCid100WithCupiLoc(selectedCupi)));
      }
      return;
    }

    FitmentArticleDto fitmentArticle = fitmentArticleOpt.get();
    if (CollectionUtils.isEmpty(article.getCriteria())) {
      article.addCriterias(fitmentArticle.getCriteria());
    }
    final List<ArticleCriteriaDto> criterias = fitmentArticle.getCriteria().stream()
        .filter(artCriteria -> fitmentPredicate().test(artCriteria, article.getCriteria()))
        .collect(Collectors.toList());
    article.addCriterias(criterias);
  }

  protected ArticleCriteriaDto createCid100WithCupiLoc(GtCUPIInfo cupis) {
    ArticleCriteriaDto cid100Criteria = new ArticleCriteriaDto();
    cid100Criteria.setCid(CID_100_BY_DEFAULT);
    cid100Criteria.setCvp(Optional.of(cupis).filter(Objects::nonNull).map(GtCUPIInfo::getLoc)
        .orElse(StringUtils.EMPTY));
    return cid100Criteria;
  }

  private BiPredicate<ArticleCriteriaDto, List<ArticleCriteriaDto>> fitmentPredicate() {
    return (artCriteria, fitmentCriterias) -> fitmentCriterias.stream().anyMatch(
        fitmentCriteria -> !duplicateCriteriaInfoPredicate().test(artCriteria, fitmentCriteria));
  }

  private BiPredicate<ArticleCriteriaDto, ArticleCriteriaDto> duplicateCriteriaInfoPredicate() {
    return (artCriteria,
        fitmentCriteria) -> StringUtils.equalsIgnoreCase(artCriteria.getCid(),
            fitmentCriteria.getCid())
            && StringUtils.equalsIgnoreCase(artCriteria.getCvp(), fitmentCriteria.getCvp())
            && StringUtils.equalsIgnoreCase(artCriteria.getCndech(), fitmentCriteria.getCndech());
  }

  public List<ArticleDocDto> combineArticlesByFitment(List<ArticleDocDto> articles,
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi, String vehId) {
    return processCombineArticlesByFitment(articles, articlesByCupi, vehId);
  }

  /**
   * Combines articles by fitments info.
   *
   * @param articles the input articles
   * @param articlesByCupi
   * @param vehId the selected vehicle id
   * @return the list filtered articles
   */
  private List<ArticleDocDto> processCombineArticlesByFitment(List<ArticleDocDto> articles,
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi, String vehId) {
    if (CollectionUtils.isEmpty(articles)) {
      return Collections.emptyList();
    }
    if (StringUtils.isBlank(vehId)) {
      return articles;
    }

    final List<String> articleIds =
        articles.stream().map(ArticleDocDto::getArtid).collect(Collectors.toList());
    final List<VehicleGenArtArtDoc> vehGenArtArtDocs =
        vehGenArtArtSearchService.searchFitmentsByVehIdAndArticleIds(vehId, articleIds);
    final List<FitmentArticleDto> fitmentArticles = distFitmentArtConverter.apply(vehGenArtArtDocs);

    combineArticles(articles, fitmentArticles, articlesByCupi);

    return articles;
  }
}