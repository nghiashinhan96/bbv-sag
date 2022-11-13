package com.sagag.services.ivds.api.impl;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.utils.DefaultAmountHelper;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.article.oil.OilProduct;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.article.BarcodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.batteries.BatteryArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.bulbs.BulbArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.oils.OilArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.MatchCodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.MotorArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.TyreArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.MatchCodeArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.MotorTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.TyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.converter.article.ExternalPartArticleConverter;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.impl.CompositeArticleFilterContext;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.BatteryArticleSearchRequest;
import com.sagag.services.ivds.request.filter.BulbArticleSearchRequest;
import com.sagag.services.ivds.request.filter.MotorTyreArticleSearchRequest;
import com.sagag.services.ivds.request.filter.OilArticleSearchRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;
import com.sagag.services.ivds.response.CustomArticleResponseDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation class for IVDS article service.
 */
@Service
@Slf4j
public class IvdsArticleServiceImpl extends ArticleProcessor implements IvdsArticleService {

  private static final boolean IS_EXCLUDE_ERP_SYNC = true;

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private CompositeArticleFilterContext articleFilterContext;

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private BarcodeArticleSearchServiceImpl barcodeArticleSearchService;

  @Autowired
  private TyreArticleSearchServiceImpl tyreSearchService;

  @Autowired
  private MotorArticleSearchServiceImpl motorTyreSearchService;

  @Autowired
  private MatchCodeArticleSearchServiceImpl matchCodeSearchService;

  @Autowired
  private BulbArticleSearchServiceImpl bulbSearchService;

  @Autowired
  private BatteryArticleSearchServiceImpl batterySearchService;

  @Autowired
  private OilArticleSearchServiceImpl oilSearchService;

  @Autowired
  protected ExternalPartArticleConverter externalPartConverter;

  @Override
  public Page<ArticleDocDto> searchArticlesByNumber(UserInfo user, String articleNr,
      int amountNumber, Pageable pageable, boolean isDeepLink) {
    final Page<ArticleDocDto> articles =
        searchArticlesByNumber(user, articleNr, isDeepLink, pageable).map(articleConverter);

    if (!articles.hasContent()) {
      return Page.empty();
    }

    if (isDeepLink && articles.getContent().size() == 1) {
      articles.forEach(
          a -> a.setAmountNumber(DefaultAmountHelper.parseAmountNumberInteger(amountNumber)));
    }

    // By pass the ERP calling
    final List<ArticleDocDto> articleDtos =
        ivdsArticleTaskExecutors.executeTaskWithoutErp(user, articles.getContent(), Optional.empty());

    return new PageImpl<>(articleDtos, pageable, articles.getTotalElements());
  }

  private Page<ArticleDoc> searchArticlesByNumber(UserInfo user, String articleNr,
      boolean isDeepLink, Pageable pageable) {
    if (isDeepLink) {
      return articleSearchService.searchArticleByNumberDeepLink(articleNr, PageUtils.MAX_PAGE);
    }
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(articleNr, findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    criteria.setUseExternalParts(user.isUsedExternalPart());
    return articleSearchService.searchArticlesByNumber(criteria, pageable);
  }

  @Override
  @LogExecutionTime
  public ArticleFilteringResponseDto searchFreetext(final FreetextSearchRequest request) {
    final String text = request.getText();
    final Pageable pageable = request.getPageRequest();
    final UserInfo user = request.getUser();

    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(text, findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());

    Optional<AdditionalSearchCriteria> additional =
        additionalCriBuilder.buildNoVehicleAdditional(IS_EXCLUDE_ERP_SYNC);
    final ArticleFilterRequest filterRequest = new ArticleFilterRequest();
    filterRequest.setFilterMode(FilterMode.FREE_TEXT.name());
    filterRequest.setKeyword(text);
    filterRequest.setFullRequest(request.isFullRequest());

    final ArticleFilteringResponseDto artFilterRes =
        articleFilterContext.execute(user, filterRequest, pageable, additional);

    artFilterRes.setArticles(new PageImpl<>(artFilterRes.getArticles().getContent(),
        artFilterRes.getArticles().getPageable(), artFilterRes.getArticles().getTotalElements()));

    return artFilterRes;
  }

  @Override
  public Page<ArticleDocDto> searchArticlesByArticleIds(UserInfo user, Set<String> pimIds,
      Optional<VehicleDto> vehicle) {
    return searchAndFillOilProductArticlesByArticleIds(user, pimIds, vehicle,
        Collections.emptyList());
  }

  @Override
  public Page<ArticleDocDto> searchAndFillOilProductArticlesByArticleIds(UserInfo user,
      Set<String> pimIds, Optional<VehicleDto> veh, List<OilProduct> oilProducts) {
    if (CollectionUtils.isEmpty(pimIds)) {
      return Page.empty();
    }
    final List<String> articleIds = new ArrayList<>(pimIds);
    final Pageable pageable = PageUtils.defaultPageable(pimIds.size());
    final Page<ArticleDocDto> articles = articleSearchService.searchArticlesByIdSagSyses(articleIds,
        pageable, user.isSaleOnBehalf(), findEsAffilateNameLocks(user)).map(articleConverter);
    if (!articles.hasContent()) {
      return Page.empty();
    }

    if (!CollectionUtils.isEmpty(oilProducts)) {
      articles.forEach(article -> oilProducts.stream()
          .filter(product -> product.containPimId(article.getIdSagsys())).findFirst()
          .ifPresent(product -> {
            article.setOilArticle(true);
            article.setOilProduct(product);
          }));
    }

    final List<ArticleDocDto> modifiableArticles = new ArrayList<>(articles.getContent());
    final Optional<AdditionalSearchCriteria> additional = additionalCriBuilder
        .buildDetailAdditional(Lists.newArrayList(pimIds), veh.map(VehicleDto::getKTypeNrStr));
    return new PageImpl<>(ivdsArticleTaskExecutors.executeTaskWithPriceAndStock(user,
        modifiableArticles, veh, additional));
  }

  @Override
  public Optional<ArticleDocDto> searchArticleByArticleId(UserInfo user, String artId,
      Optional<VehicleDto> vehicle) {
    if (StringUtils.isBlank(artId)) {
      return Optional.empty();
    }
    final Page<ArticleDocDto> articles =
        articleSearchService.searchArticlesByIdSagSys(artId, PageUtils.FIRST_ITEM_PAGE,
            user.isSaleOnBehalf(), findEsAffilateNameLocks(user)).map(articleConverter);
    final List<ArticleDocDto> modifiableArticles = new ArrayList<>(articles.getContent());
    return ivdsArticleTaskExecutors.executeTaskWithPriceAndStock(user, modifiableArticles, vehicle,
        additionalCriBuilder.buildDetailAdditional(artId)).stream().findFirst();
  }

  @Override
  public Page<ArticleDocDto> searchArticleByArticleIds(UserInfo user, List<String> artIds) {
    if (CollectionUtils.isEmpty(artIds)) {
      return Page.empty();
    }
    return new PageImpl<>(articleSearchService.searchArticlesByIdSagSyses(artIds,
        user.isSaleOnBehalf(), findEsAffilateNameLocks(user))).map(articleConverter);
  }

  @Override
  public Page<ArticleDocDto> searchArticlesByBarCode(final UserInfo user, final String code) {
    Assert.hasText(code, "The bar code should not be null or blank");
    final KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria(code, findEsAffilateNameLocks(user));

    Page<ArticleDocDto> articles =
        barcodeArticleSearchService.search(criteria, PageUtils.MAX_PAGE).map(articleConverter);

    return new PageImpl<>(ivdsArticleTaskExecutors.executeTaskWithPriceAndStockWithoutVehicle(user,
        articles.getContent(), additionalCriBuilder.buildNoVehicleAdditional(code)));
  }

  @Override
  public ArticleDocDto searchVinArticle(UserInfo user, String licenseArticleId) {
    log.debug("Returning VIN article by license article id = {}", licenseArticleId);
    return findSpecialArticleFromErp(user, licenseArticleId).orElse(null);
  }

  @Override
  public Optional<Double> getCounponPrice(UserInfo user, String couponArticleId) {
    log.debug("Returning counpon article by coupon article id = {}", couponArticleId);
    final Optional<ArticleDocDto> article = findSpecialArticleFromErp(user, couponArticleId);
    if (!article.isPresent()) {
      return Optional.empty();
    }
    return article.get().getNetPrice();
  }

  private Optional<ArticleDocDto> findSpecialArticleFromErp(UserInfo user,
      String specialArticleId) {
    final ArticleDocDto article = new ArticleDocDto();
    article.setId(specialArticleId);
    article.setArtid(specialArticleId);
    article.setIdSagsys(specialArticleId);
    article.setAmountNumber(NumberUtils.INTEGER_ONE);

    final List<ArticleDocDto> articles = ivdsArticleTaskExecutors.executeTaskWithPrice(user,
        Arrays.asList(article), Optional.empty(), Optional.empty());
    return CollectionUtils.emptyIfNull(articles).stream().findFirst();
  }

  @Override
  public ArticleFilteringResponseDto searchArticlesByFilteringRequest(final UserInfo user,
      final ArticleFilterRequest request, final Pageable pageable) {
    // Filtering article with mode: FREE_TEXT, ARTICLE_NUMBER, ID_SAGSYS,
    // TYRES_SEARCH, MOTOR_TYRES_SEARCH, BULB_SEARCH, OIL_SEARCH, EAN_CODE, BAR_CODE, CROSS_REFERENCE
    Optional<AdditionalSearchCriteria> additionalSearchCriteria =
        additionalCriBuilder.buildNoVehicleAdditional(request.getKeyword(), request.getFinalCustomerId(),
            !FilterMode.TYRES_SEARCH.name().equals(request.getFilterMode()));
    final ArticleFilteringResponseDto resultSearch =
        articleFilterContext.execute(user, request, pageable, additionalSearchCriteria);
    doUpdateFlagFavorite(user, resultSearch);
    return resultSearch;
  }

  @Override
  public CustomArticleResponseDto searchTyreArticlesByRequest(final UserInfo user,
      final TyreArticleSearchRequest request, final Pageable pageable) {
    return searchTyreArticles(user, request, pageable);
  }

  private CustomArticleResponseDto searchTyreArticles(final UserInfo user,
      final TyreArticleSearchRequest request, final Pageable pageable) {
    if (request.isMatchCodeSearch()) {
      return new CustomArticleResponseDto(
          matchCodeSearch(user).apply(request.getMatchCode(), pageable));
    }
    final TyreArticleSearchCriteria criteria = request.toCriteria();
    criteria.setAffNameLocks(findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    return new CustomArticleResponseDto(tyreSearchService.search(criteria, pageable));
  }

  @Override
  public CustomArticleResponseDto searchMotorTyreArticlesByRequest(final UserInfo user,
      final MotorTyreArticleSearchRequest request, final PageRequest pageable) {
    return searchMotorTyreArticles(user, request, pageable);
  }

  private CustomArticleResponseDto searchMotorTyreArticles(final UserInfo user,
      final MotorTyreArticleSearchRequest request, final PageRequest pageable) {
    if (request.isMatchCodeSearch()) {
      return new CustomArticleResponseDto(
          matchCodeSearch(user).apply(request.getMatchCode(), pageable));
    }
    final MotorTyreArticleSearchCriteria criteria = request.toCriteria();
    criteria.setAffNameLocks(findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    return new CustomArticleResponseDto(motorTyreSearchService.search(criteria, pageable));
  }

  private BiFunction<String, Pageable, TyreArticleResponse> matchCodeSearch(final UserInfo user) {
    return (matchCode, pageable) -> {
      MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
      criteria.setAffNameLocks(findEsAffilateNameLocks(user));
      criteria.setMatchCode(matchCode);
      criteria.setSaleOnBehalf(user.isSaleOnBehalf());
      return matchCodeSearchService.search(criteria, pageable);
    };
  }

  @Override
  public CustomArticleResponseDto searchBulbArticlesByRequest(UserInfo user,
      BulbArticleSearchRequest request, Pageable pageable) {
    log.debug("Search bulb articles from search request {} - pageable = {}", request, pageable);
    final BulbArticleSearchCriteria criteria = request.toCriteria();
    criteria.setAffNameLocks(findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    return new CustomArticleResponseDto(bulbSearchService.search(criteria, pageable));
  }

  @Override
  public CustomArticleResponseDto searchBatteryArticlesByRequest(final UserInfo user,
      final BatteryArticleSearchRequest request, final Pageable pageable) {
    final BatteryArticleSearchCriteria criteria = request.toCriteria();
    criteria.setAffNameLocks(findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    return new CustomArticleResponseDto(batterySearchService.search(criteria, pageable));
  }

  @Override
  public CustomArticleResponseDto searchOilArticlesByRequest(UserInfo user,
      OilArticleSearchRequest request, Pageable pageable) {
    log.debug("Search oil articles from search request {} - pageable = {}", request, pageable);
    final OilArticleSearchCriteria criteria = request.toCriteria();
    criteria.setAffNameLocks(findEsAffilateNameLocks(user));
    criteria.setSaleOnBehalf(user.isSaleOnBehalf());
    return new CustomArticleResponseDto(oilSearchService.search(criteria, pageable));
  }
}
