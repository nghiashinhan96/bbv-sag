package com.sagag.services.ivds.api.impl;

import com.google.common.base.MoreObjects;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.MakeItem;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.domain.CategoryDoc;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.domain.article.ArticlePart;
import com.sagag.services.elasticsearch.domain.category.GtCUPIInfo;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.hazelcast.api.CategoryCacheService;
import com.sagag.services.hazelcast.api.MakeCacheService;
import com.sagag.services.ivds.api.IvdsArticleInVehicleContextService;
import com.sagag.services.ivds.api.IvdsGtmotiveSearchService;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.executor.IvdsArticleVehicleInContextTaskExecutor;
import com.sagag.services.ivds.filter.gtmotive.GtmotiveLeadingZeroIdsFilter;
import com.sagag.services.ivds.filter.gtmotive.NonMatchedOperationsFilter;
import com.sagag.services.ivds.request.CategorySearchRequest;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationItem;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import com.sagag.services.ivds.request.vehicle.GTmotiveDirectMatches;
import com.sagag.services.ivds.response.ArticleListSearchResponseDto;
import com.sagag.services.ivds.response.GTmotiveArticleDocDto;
import com.sagag.services.ivds.response.GtmotiveCupisResponse;
import com.sagag.services.ivds.response.GtmotiveResponse;
import com.sagag.services.ivds.utils.ArticlesLocationUtils;
import com.sagag.services.ivds.utils.FitmentArticleCombinator;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@GtmotiveProfile
@Service
public class IvdsGtmotiveSearchServiceImpl extends ArticleProcessor
    implements IvdsGtmotiveSearchService {

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private MakeCacheService makeCacheService;

  @Autowired
  private CategoryCacheService cateCacheService;

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Autowired
  private IvdsArticleInVehicleContextService ivdsArticleInVehicleContextService;

  @Autowired
  private IvdsArticleVehicleInContextTaskExecutor ivdsArticleVehInContextTaskExecutor;

  @Autowired
  private NonMatchedOperationsFilter nonMatchedOperationsFilter;

  @Autowired
  private GtmotiveLeadingZeroIdsFilter gtmotiveLeadingZeroIdsFilter;

  @Autowired
  private FitmentArticleCombinator fitmentArticleCombinator;

  @Override
  public GtmotiveResponse searchArticlesByGtOperations(final UserInfo user,
      final GtmotiveOperationRequest request) {
    log.debug("Returning articles by GT Operations by request = {}", request);
    final List<GtmotiveOperationItem> gtOperations = request.getOperations();
    final String makeCode = request.getMakeCode();

    final String vehicleId = request.getVehicleId();
    final Optional<VehicleDto> gtmotiveVehicle = ivdsVehicleService.searchVehicleByVehId(vehicleId);

    final GtmotiveResponse gtmotiveResponse = new GtmotiveResponse();
    gtmotiveVehicle.ifPresent(gtmotiveResponse::setVehicle);
    return articleResponseProcessor(gtOperations, makeCode, vehicleId, request.getCupis(),
        request.isUsingVersion2()).apply(user, gtmotiveResponse);
  }

  private BiFunction<UserInfo, GtmotiveResponse, GtmotiveResponse> articleResponseProcessor(
      final List<GtmotiveOperationItem> operations, final String makeCode, final String vehicleId,
      final List<String> cupis, final boolean usingVer2) {
    return (user, gtmotiveResponse) -> {
      gtmotiveResponse = MoreObjects.firstNonNull(gtmotiveResponse, new GtmotiveResponse());

      // #991: Interface:GTMotive- leading zeros
      final List<String> partNumbers = gtmotiveLeadingZeroIdsFilter.filter(operations);
      Collection<CategoryDoc> catesFromCupis = cateCacheService.findCategoriesByCupis(cupis);
      List<String> cateIdsFromCupis = CollectionUtils.emptyIfNull(catesFromCupis).stream()
          .map(CategoryDoc::getId).collect(Collectors.toList());
      List<ArticleDocDto> cupiArticles = searchPartsByCupis(cupis, catesFromCupis, user, vehicleId);
      if (CollectionUtils.isEmpty(partNumbers)) {
        // if empty, then search by generic articles found with CUPI (short number), phase 2
        gtmotiveResponse.setCupisResponse(GtmotiveCupisResponse.builder().cateIds(cateIdsFromCupis)
            .articles(new PageImpl<>(cupiArticles)).build());
        gtmotiveResponse.setArticles(new PageImpl<>(Collections.emptyList()));
        return gtmotiveResponse;
      }

      // Get make item from cache in ES
      final Optional<MakeItem> makeItem = makeCacheService.findMakeItemByCode(makeCode);

      // Get the categories of selected vehicle
      final String vehId = gtmotiveResponse.getVehicleId();

      // #3625 - [AX] Normauto for customers
      gtmotiveResponse.setNormauto(
          user.isNormautoDisplay() && makeItem.map(MakeItem::isNormauto).orElse(false));

      // Get make Id to query in ES
      final String makeId = makeItem.map(MakeItem::getDefaultMakeIdStr).orElse(StringUtils.EMPTY);

      // filter articles for OEM type and part branch id
      List<String> cupisFromReferences =
          operations.stream().map(GtmotiveOperationItem::getCupi).collect(Collectors.toList());
      Collection<CategoryDoc> catesByCupis =
          cateCacheService.findCategoriesByCupis(cupisFromReferences);

      Map<GtmotiveOperationItem, List<String>> gaidsByOps = new HashMap<>();

      ListUtils.emptyIfNull(operations)
          .forEach(operation -> gaidsByOps.put(operation, ListUtils
              .emptyIfNull(
                  cateCacheService.searchGenArtIdsByPartCodes(Arrays.asList(operation.getCupi())))
              .stream().distinct().collect(Collectors.toList())));

      List<ArticleDocDto> articles = new ArrayList<>();
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi = new HashMap<>();
      List<GTmotiveDirectMatches> gtmotiveDirectMatches = new ArrayList<>();

      MapUtils.emptyIfNull(gaidsByOps).forEach((operation, gaids) -> {
        List<String> leadingZeroPartNumber =
            gtmotiveLeadingZeroIdsFilter.filter(Arrays.asList(operation));
        List<ArticleDoc> articlesByRefs = searchArticlesByReferences(user, partNumbers);
        List<ArticleDocDto> articlesBySpecificCupi = searchArticlesByGaidsReferencesAndBranchId(
            articlesByRefs, leadingZeroPartNumber, makeId, gaids);
        if (CollectionUtils.isEmpty(articlesBySpecificCupi)) {
          List<ArticleDocDto> directMatchesArticles =
              searchArticlesByReferencesAndBranchId(articlesByRefs, leadingZeroPartNumber, makeId);
          if (CollectionUtils.isNotEmpty(directMatchesArticles)) {
            gtmotiveDirectMatches.add(GTmotiveDirectMatches.builder()
                .directMatchesArticles(
                    directMatchesArticles.stream().distinct().collect(Collectors.toList()))
                .pseudoCategoryName(operation.getDescription()).reference(operation.getReference())
                .build());
          }
        } else {
          articles.addAll(articlesBySpecificCupi);
          articlesBySpecificCupi.forEach(
              article -> findArticlesByCupi(articlesByCupi, article, operation, catesByCupis));
        }
      });

      List<ArticleDocDto> distinctArticles =
          ListUtils.emptyIfNull(articles).stream().distinct().collect(Collectors.toList());

      List<ArticleDocDto> combinedArticles = fitmentArticleCombinator
          .combineArticlesByFitment(distinctArticles, articlesByCupi, vehId);

      final Optional<VehicleDto> vehicleOpt = Optional.ofNullable(gtmotiveResponse.getVehicle());
      final Optional<AdditionalSearchCriteria> vehicleAdditional =
          additionalCriBuilder.buildVehicleAdditional(vehicleOpt.map(VehicleDto::getKTypeNrStr));

      if (CollectionUtils.isNotEmpty(gtmotiveDirectMatches)) {
        List<ArticleDocDto> allDirectmatchesArticles =
            gtmotiveDirectMatches.stream().map(GTmotiveDirectMatches::getDirectMatchesArticles)
                .flatMap(Collection::stream).distinct().collect(Collectors.toList());

        List<ArticleDocDto> erpSynDirectMatchesArticles = ivdsArticleVehInContextTaskExecutor
            .execute(user, allDirectmatchesArticles, vehicleOpt, vehicleAdditional, usingVer2);
        gtmotiveDirectMatches.forEach(gt -> {
          List<String> articlesIds = gt.getDirectMatchesArticles().stream()
              .map(ArticleDocDto::getArtid).collect(Collectors.toList());
          List<ArticleDocDto> erpSynArticlesByReference =
              CollectionUtils.emptyIfNull(erpSynDirectMatchesArticles).stream()
                  .filter(erpArt -> articlesIds.contains(erpArt.getArtid()))
                  .collect(Collectors.toList());
          gt.setDirectMatchesArticles(erpSynArticlesByReference);
        });
        gtmotiveResponse.setDirectMatches(new PageImpl<>(gtmotiveDirectMatches));
      }

      List<String> referencesFromFoundDirectMatches =
          CollectionUtils.emptyIfNull(gtmotiveDirectMatches).stream()
              .map(GTmotiveDirectMatches::getReference).collect(Collectors.toList());
      List<GtmotiveOperationItem> nonMatchOperations = ListUtils.emptyIfNull(operations).stream()
          .filter(op -> !referencesFromFoundDirectMatches.contains(op.getReference()))
          .collect(Collectors.toList());
      gtmotiveResponse.setNonMatchedOperations(
          nonMatchedOperationsFilter.filter(vehId, articles, nonMatchOperations, partNumbers));

      List<ArticleDocDto> result = ivdsArticleVehInContextTaskExecutor.execute(user,
          combinedArticles, vehicleOpt, vehicleAdditional, usingVer2);

      List<GTmotiveArticleDocDto> articleWithCupi =
          attachCupiToArticleResult(articlesByCupi, result);

      gtmotiveResponse.setCupisResponse(GtmotiveCupisResponse.builder().cateIds(cateIdsFromCupis)
          .articles(new PageImpl<>(cupiArticles)).build());
      gtmotiveResponse.setArticles(new PageImpl<>(articleWithCupi));
      return gtmotiveResponse;
    };
  }

  private static void findArticlesByCupi(Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi,
      ArticleDocDto article, GtmotiveOperationItem operation,
      Collection<CategoryDoc> catesByCupis) {

    final String selectedCUPI = operation.getCupi();
    final List<GtCUPIInfo> filterCUPIInfoListByGaId =
        catesByCupis.stream().flatMap(cate -> ListUtils.emptyIfNull(cate.getGenarts()).stream())
            .filter(genArt -> StringUtils.equals(genArt.getGaid(), article.getGaId()))
            .flatMap(genArt -> ListUtils.emptyIfNull(genArt.getCupis()).stream())
            .filter(cupi -> StringUtils.equals(cupi.getCupi(), selectedCUPI))
            .collect(Collectors.toList());

    if (CollectionUtils.size(filterCUPIInfoListByGaId) > 1) {
      log.warn("Found more than one CUPI information by gaid = {} - selectedCUPI = {}",
          article.getGaId(), selectedCUPI);
    }

    final Optional<GtCUPIInfo> matchedCUPIInfoOpt = filterCUPIInfoListByGaId.stream().findFirst();
    final BiFunction<GtCUPIInfo, List<ArticleDocDto>, List<ArticleDocDto>> articlesByCupiMapper;
    articlesByCupiMapper = (gtCupiInfo, artList) -> {
      if (artList == null) {
        artList = new ArrayList<>();
      }
      artList.add(article);
      return artList;
    };
    matchedCUPIInfoOpt.ifPresent(
        matchedCUPIInfo -> articlesByCupi.compute(matchedCUPIInfo, articlesByCupiMapper));
  }

  private List<GTmotiveArticleDocDto> attachCupiToArticleResult(
      Map<GtCUPIInfo, List<ArticleDocDto>> articlesByCupi, List<ArticleDocDto> articles) {
    List<GTmotiveArticleDocDto> result = new ArrayList<>();
    ListUtils.emptyIfNull(articles).forEach(article -> {
      GTmotiveArticleDocDto cupiArt = SagBeanUtils.map(article, GTmotiveArticleDocDto.class);
      cupiArt.setCupi(MapUtils.emptyIfNull(articlesByCupi).entrySet().stream()
          .filter(artByCupi -> artByCupi.getValue().stream()
              .anyMatch(art -> StringUtils.equals(art.getArtid(), article.getArtid())))
          .map(Entry::getKey).findFirst().orElse(null));
      result.add(cupiArt);
    });
    return result;
  }

  private List<ArticleDocDto> searchArticlesByGaidsReferencesAndBranchId(List<ArticleDoc> articles,
      List<String> partNumbers, String makeId, List<String> gaids) {
    return CollectionUtils.emptyIfNull(articles).stream().filter(gaIdPredicate(gaids))
        .filter(oemPartNumberArticlePredicate(makeId, partNumbers)).map(articleConverter)
        .collect(Collectors.toList());
  }

  // Direct mathces for GT motive
  // https://sagdigital.atlassian.net/browse/CONNECT-48
  private List<ArticleDocDto> searchArticlesByReferencesAndBranchId(List<ArticleDoc> articles,
      List<String> partNumbers, String makeId) {
    return CollectionUtils.emptyIfNull(articles).stream()
        .filter(oemPartNumberArticlePredicate(makeId, partNumbers)).map(articleConverter)
        .collect(Collectors.toList());
  }
  
  private List<ArticleDoc> searchArticlesByReferences(UserInfo user, List<String> partNumbers) {
    final String[] lockedAffNames = findEsAffilateNameLocks(user);
    return articleSearchService.searchArticlesByPartRefs(partNumbers, user.isSaleOnBehalf(),
        lockedAffNames);
  }

  private Predicate<ArticleDoc> gaIdPredicate(List<String> gaids) {
    return article -> CollectionUtils.isNotEmpty(gaids) && gaids.contains(article.getGaId());
  }

  private static Predicate<ArticleDoc> oemPartNumberArticlePredicate(final String makeId,
      final List<String> partNumbers) {
    return article -> {
      List<ArticlePart> parts = ListUtils.union(article.getPartsExt(), article.getParts());
      return CollectionUtils.emptyIfNull(parts).stream().distinct()
          .anyMatch(oemPartNumberArticlePartPredicate(makeId, partNumbers));
    };
  }

  private static Predicate<ArticlePart> oemPartNumberArticlePartPredicate(final String makeId,
      final List<String> partNumbers) {
    return part -> partNumbers.contains(part.getPnrn())
        && StringUtils.equals(ArticlePartType.OEM.name(), part.getPtype())
        && StringUtils.equals(makeId, part.getBrandid());
  }

  private List<ArticleDocDto> searchPartsByCupis(final List<String> cupis,
      Collection<CategoryDoc> cates, UserInfo user, String vehicleId) {

    if (CollectionUtils.isEmpty(cupis) || StringUtils.isEmpty(vehicleId)) {
      return Collections.emptyList();
    }
    List<String> gaIds = cateCacheService.searchGenArtIdsByPartCodes(cupis);
    if (CollectionUtils.isEmpty(gaIds)) {
      return Collections.emptyList();
    }

    CategorySearchRequest categorySearchRequest =
        CategorySearchRequest.builder().genArtIds(gaIds).vehIds(Arrays.asList(vehicleId)).build();

    ArticleListSearchResponseDto partListSearchResultDto;
    try {
      partListSearchResultDto = ivdsArticleInVehicleContextService
          .searchArticlesInVehicleContext(user, categorySearchRequest);
    } catch (ServiceException e) {
      log.error("Error while searching green parts", e);
      return Collections.emptyList();
    }

    if (Objects.isNull(partListSearchResultDto)
        || Objects.isNull(partListSearchResultDto.getArticles())
        || !partListSearchResultDto.getArticles().hasContent()) {
      return Collections.emptyList();
    }

    return ArticlesLocationUtils.filterArticlesByLocation(cates,
        partListSearchResultDto.getArticles().getContent());
  }
}
