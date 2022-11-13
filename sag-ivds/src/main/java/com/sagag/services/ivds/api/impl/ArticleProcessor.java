package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.api.SupportedBrandPromotionRepository;
import com.sagag.eshop.repo.entity.SupportedAffiliate;
import com.sagag.eshop.repo.enums.ArticleShopType;
import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.ExternalPartsSearchService;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.ivds.converter.article.ExternalPartArticleConverter;
import com.sagag.services.ivds.response.ArticleListSearchResponseDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * Implementation for article support processor .
 */
public class ArticleProcessor {

  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Autowired
  private SupportedBrandPromotionRepository supportedBrandPromotionRepo;

  @Autowired
  private EshopFavoriteService favoriteBusinessService;

  @Autowired
  protected AdditionalCriteriaBuilder additionalCriBuilder;

  @Autowired
  protected ArticleConverter articleConverter;

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private ExternalPartsSearchService externalPartsSearchService;

  @Autowired
  private ExternalPartArticleConverter externalPartConverter;

  public String[] findEsAffilateNameLocks(final UserInfo user) {
    // #1814: AX-Sales: Scope locks to country
    // if is sales on behalf
    if (user.isSaleOnBehalf()) {
      return supportedAffiliateRepo.findByWithinCountryOfShortName(user.getAffiliateShortName())
          .stream()
          .map(SupportedAffiliate::getEsShortName)
          .map(StringUtils::trim)
          .toArray(String[]::new);
    }
    return supportedAffiliateRepo.findFirstByShortName(user.getAffiliateShortName())
        .map(SupportedAffiliate::getEsShortName)
        .map(StringUtils::trim)
        .map(ArrayUtils::toArray)
        .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  public List<String> getNowPromotionBrands(Optional<ArticleShopType> shopType, String affiliate) {
    if (StringUtils.isBlank(affiliate) || !shopType.isPresent()) {
      return Collections.emptyList();
    }
    final DateTime now = DateTime.now().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
        .withSecondOfMinute(0).withMillisOfSecond(0);
    return supportedBrandPromotionRepo.findPromotionBrands(shopType.get(), affiliate, now.toDate());
  }

  protected void doUpdateFlagFavorite(UserInfo user,
      ArticleFilteringResponseDto resultSearch) {
    if (resultSearch == null) {
      return;
    }
    doUpdateFlagFavorite(user, resultSearch.getArticles());
  }

  protected void doUpdateFlagFavorite(UserInfo user,
      ArticleListSearchResponseDto resultSearch) {
    if (resultSearch == null) {
      return;
    }
    doUpdateFlagFavorite(user, resultSearch.getArticles());
  }

  private void doUpdateFlagFavorite(UserInfo user, final Slice<ArticleDocDto> articles) {
    List<ArticleDocDto> arts = Optional.ofNullable(articles).map(Slice::getContent)
        .orElse(Collections.emptyList());
    favoriteBusinessService.updateFavoriteFlagArticles(user, arts);
  }

  protected Page<ArticleDocDto> searchArticlesByArtIdsAndExternalPartArtId(List<String> artIds,
      boolean isSaleOnBehalf) {
    if (CollectionUtils.isEmpty(artIds)) {
      return Page.empty();
    }
    final List<String> filteredArtIds = artIds.stream().filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    final Pageable pageable = PageUtils.defaultPageable(filteredArtIds.size());

    final Page<ArticleDocDto> articles;
    if (CollectionUtils.size(artIds) == 1) {
      articles = articleSearchService.searchArticlesByIdSagSys(artIds.get(0), pageable,
          isSaleOnBehalf).map(articleConverter);
    } else {
      articles = articleSearchService.searchArticlesByIdSagSyses(filteredArtIds, pageable,
              isSaleOnBehalf).map(articleConverter);
    }

    final Set<String> existedArtIds = articles.map(ArticleDocDto::getIdSagsys).getContent()
        .stream().collect(Collectors.toSet());
    final List<String> extArtIds = filteredArtIds.stream()
        .filter(artId -> !existedArtIds.contains(artId))
        .collect(Collectors.toList());
    final List<ArticleDocDto> extPartArticles = externalPartsSearchService.searchByArtIds(extArtIds)
        .stream().map(externalPartConverter).collect(Collectors.toList());

    final List<ArticleDocDto> fullArticles = Stream.of(articles.getContent(), extPartArticles)
        .flatMap(List::stream).collect(Collectors.toList());

    return new PageImpl<>(fullArticles, pageable, fullArticles.size());
  }
}
